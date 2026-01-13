package com.ranyk.authorization.service.role;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.ranyk.authorization.repository.role.RoleRepository;
import com.ranyk.authorization.service.account.AccountRoleConnectionService;
import com.ranyk.common.constant.AccountPermissionEnum;
import com.ranyk.common.constant.RoleStatusEnum;
import com.ranyk.model.base.dto.BaseDTO;
import com.ranyk.model.business.account.dto.AccountRoleConnectionDTO;
import com.ranyk.model.business.role.dto.RoleDTO;
import com.ranyk.model.business.role.dto.RolePermissionConnectionDTO;
import com.ranyk.model.business.role.entity.Role;
import com.ranyk.model.business.role.vo.RoleVO;
import com.ranyk.model.exception.service.ServiceException;
import com.ranyk.model.page.vo.PageVO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: RoleService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色业务逻辑处理类
 * @date: 2025-12-18
 */
@Slf4j
@Service
public class RoleService {

    /**
     * 账号角色关联信息业务逻辑类
     */
    private final AccountRoleConnectionService accountRoleConnectionService;
    /**
     * 角色权限关联信息业务逻辑类
     */
    private final RolePermissionsConnectionService rolePermissionsConnectionService;
    /**
     * 角色信息数据库操作类
     */
    private final RoleRepository roleRepository;

    /**
     * 构造函数
     *
     * @param accountRoleConnectionService     账号角色关联信息业务逻辑类
     * @param rolePermissionsConnectionService 角色权限关联信息业务逻辑类
     * @param roleRepository                   角色信息数据库操作类
     */
    @Autowired
    public RoleService(AccountRoleConnectionService accountRoleConnectionService,
                       RolePermissionsConnectionService rolePermissionsConnectionService,
                       RoleRepository roleRepository) {
        this.accountRoleConnectionService = accountRoleConnectionService;
        this.rolePermissionsConnectionService = rolePermissionsConnectionService;
        this.roleRepository = roleRepository;
    }

    /**
     * 获取当前登录账户的角色信息
     *
     * @return 返回当前登录账户的角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 对象
     */
    public List<RoleDTO> getCurrentUsersRoleList() {
        // 1. 获取当前登录账户的账户 ID, 将其转换为 Long 类型
        Long longinId = StpUtil.getLoginIdAsLong();
        // 2. 通过账户 ID 获取该账户下拥有的角色信息
        return getRoleListByAccountId(longinId);
    }

    /**
     * 通过账户 ID 获取该账户下拥有的角色信息
     *
     * @param accountId 需要查询角色信息的账户 ID
     * @return 返回该账户下的角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 角色信息对象; 当未传入账户 ID 时,则返回空角色列表; 当未查询到该账户下的角色信息时,则返回空角色列表;
     */
    public List<RoleDTO> getRoleListByAccountId(Long accountId) {
        // 1. 当传入的账户 ID 为 null,则返回空 List 集合, 说明没有角色 列表
        if (Objects.isNull(accountId)) {
            log.error("未传入的账户 ID, 不进行角色信息查询逻辑,直接返回空角色列表!");
            return Collections.emptyList();
        }
        // 2. 通过传入的 账户 ID 获取该 账户ID 下拥有的 角色 ID
        List<AccountRoleConnectionDTO> accountRoleConnectionList = accountRoleConnectionService.queryAccountRoleConnectionByAccountId(accountId);
        // 3. 从 accountRoleConnectionList 中获取 roleId List 集合
        List<Long> roleIds = accountRoleConnectionList.stream().map(AccountRoleConnectionDTO::getRoleId).toList();
        // 4. 当 roleIds 为空,则返回空 List 集合, 说明没有角色 列表
        if (roleIds.isEmpty()) {
            log.error("未获取到该账户下的角色 ID, 直接返回空角色列表!");
            return Collections.emptyList();
        }
        // 5. 通过角色 ID 获取对应的角色信息 List 集合
        List<Role> roleInfoList = Optional.of(roleRepository.findAllById(roleIds)).orElse(Collections.emptyList());
        // 6. 获取角色信息 List 集合,并将其转换为 RoleDTO 列表
        return BeanUtil.copyToList(roleInfoList, RoleDTO.class);
    }

    /**
     * 新增角色信息
     *
     * @param roleDTOList 新增角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 角色信息对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRole(List<RoleDTO> roleDTOList) {
        // 1. 判断当前用户是否有权限新增角色信息
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_ROLE_INFO.getCode())) {
            throw new ServiceException("no.create.permission");
        }
        // 2. 判断当前是否存在需要新增的角色信息
        if (Objects.isNull(roleDTOList) || roleDTOList.isEmpty()) {
            log.error("未传入需要新增的角色信息, 不进行角色信息新增逻辑!");
            throw new ServiceException("no.data.need.create");
        }
        // 3. 判断当前传入的角色中是否存在角色代码已存在于数据库中
        List<String> roleCodeList = roleDTOList.stream().map(RoleDTO::getCode).filter(StrUtil::isBlank).toList();
        // 4. 执行查询数据是否存在
        if (roleRepository.existsByCodeIn(roleCodeList)) {
            throw new ServiceException("duplicate.data.found");
        }
        // 5. 获取当前登录账户的账户 ID
        Long loginId = StpUtil.getLoginIdAsLong();
        // 6. 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 7. 组装对应的角色信息数据
        List<Role> needSaveRoleList = roleDTOList.stream().map(roleDTO -> Role.builder().name(roleDTO.getName()).code(roleDTO.getCode()).status(RoleStatusEnum.NORMAL.getCode()).createId(loginId).updateId(loginId).createTime(now).updateTime(now).build()).collect(Collectors.toList());
        // 8. 保存角色信息
        List<Role> saveRoleList = roleRepository.saveAllAndFlush(needSaveRoleList);
        // 9. 判断是否保存一致
        if (!Objects.equals(saveRoleList.size(), roleDTOList.size())) {
            log.error("新增角色信息失败,需新增的角色数量为: {} ,实际新增角色数量为: {}", roleDTOList.size(), needSaveRoleList.size());
            throw new ServiceException("create.data.fail");
        }
        // 10. 输出日志
        log.info("新增角色信息成功,需新增的角色数量为: {} ,实际新增角色数量为: {}", roleDTOList.size(), saveRoleList.size());
    }

    /**
     * 删除角色信息
     *
     * @param roleDTO 删除角色信息对象, 此方法中使用的是 {@link RoleDTO} 角色信息封装对象的父类 {@link BaseDTO} 的附加属性 ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(RoleDTO roleDTO) {
        // 1. 判断当前用户是否存在删除角色权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_ROLE_INFO.getCode())) {
            throw new ServiceException("no.delete.permission");
        }
        // 2. 判断是否存在需要删除的数据
        if (Objects.isNull(roleDTO.getIds()) || roleDTO.getIds().isEmpty()) {
            log.error("未传入需要删除的角色信息, 不进行角色信息删除逻辑!");
            throw new ServiceException("no.data.need.delete");
        }
        // 3. 执行更新数据操作
        int updateCount = roleRepository.updateRoleStatusByIds(roleDTO.getIds(), RoleStatusEnum.DELETED.getCode(), StpUtil.getLoginIdAsLong(), LocalDateTime.now());
        // 4. 输出日志
        log.info("删除角色信息成功, 需删除的角色数量为: {} , 实际删除角色数量为: {}", roleDTO.getIds().size(), updateCount);
    }

    /**
     * 修改角色信息
     *
     * @param roleDTOList 修改角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 角色信息对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(List<RoleDTO> roleDTOList) {
        // 1. 判断当前用户是否存在对应的 修改角色权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.UPDATE_ROLE_INFO.getCode())) {
            throw new ServiceException("no.update.permission");
        }
        // 2. 判断当前传入的修改角色信息是否为空
        if (Objects.isNull(roleDTOList) || roleDTOList.isEmpty()) {
            log.error("未传入需要修改的角色信息, 不进行角色信息修改逻辑!");
            throw new ServiceException("no.data.need.update");
        }
        // 3. 判断数据的完整性
        roleDTOList.forEach(roleDTO -> {
            if (Objects.isNull(roleDTO.getId())) {
                throw new ServiceException("data.incomplete");
            } else {
                if (StrUtil.isBlank(roleDTO.getName())
                        && StrUtil.isBlank(roleDTO.getCode())
                        && Objects.isNull(roleDTO.getStatus())) {
                    throw new ServiceException("data.incomplete");
                }
            }
        });
        // 4. 判断是否已经存在指定的角色代码存在,但是数据ID 不是当前的数据 ID
        List<String> roleCodeList = roleDTOList.stream().map(RoleDTO::getCode).filter(StrUtil::isNotBlank).toList();
        List<Long> idList = roleDTOList.stream().map(RoleDTO::getId).filter(Objects::nonNull).toList();
        if (roleRepository.existsByCodeInAndIdNotInAndStatusEquals(roleCodeList, idList, RoleStatusEnum.NORMAL.getCode())) {
            throw new ServiceException("duplicate.data.found");
        }
        // 5. 获取当前登录账户的账户 ID
        Long loginId = StpUtil.getLoginIdAsLong();
        // 6. 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 7. 将需要保持的角色数据 List 转换为 Map 集合,方便后续的数据组装
        Map<Long, RoleDTO> needSaveRoleMap = roleDTOList.stream().filter(roleDTO -> Objects.nonNull(roleDTO.getId())).collect(Collectors.toMap(
                RoleDTO::getId,
                roleDTO -> roleDTO,
                (existing, replacement) -> existing
        ));
        // 8. 获取当前在数据库中以及持久化的角色数据 List 集合
        List<Role> roleList = Optional.of(roleRepository.findAllById(idList)).orElse(Collections.emptyList());
        // 9. 遍历对应的角色数据 List 集合, 组装对应的角色信息
        roleList.forEach(role -> {
            RoleDTO roleDTO = needSaveRoleMap.get(role.getId());
            if (StrUtil.isNotBlank(roleDTO.getName())) {
                role.setName(roleDTO.getName());
            }
            if (StrUtil.isNotBlank(roleDTO.getCode())) {
                role.setCode(roleDTO.getCode());
            }
            if (Objects.nonNull(roleDTO.getStatus())) {
                role.setStatus(roleDTO.getStatus());
            }
            role.setUpdateId(loginId);
            role.setUpdateTime(now);
        });
        // 10、执行数据更新
        List<Role> roles = roleRepository.saveAll(roleList);
        // 11. 判断是否将其对应的数据修改成功
        if (!Objects.equals(roles.size(), roleDTOList.size())) {
            log.error("修改角色信息失败,需修改的角色数量为: {} ,实际修改角色数量为: {}", roleDTOList.size(), roles.size());
            throw new ServiceException("update.data.fail");
        }
        // 12. 输出日志
        log.info("修改角色信息成功, 需修改的角色数量为: {} ,实际修改角色数量为: {}", roleDTOList.size(), roles.size());
    }

    /**
     * 查询角色列表
     *
     * @param roleDTO 查询角色信息查询条件数据封装对象, 参见 {@link RoleDTO}
     * @return 查询结果 List 集合, 单个参见 {@link RoleVO}
     */
    public PageVO<List<RoleVO>> queryRoleList(RoleDTO roleDTO) {
        // 1. 判断当前是否拥有角色的查询权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.QUERY_ROLE_INFO.getCode())) {
            throw new ServiceException("no.view.permission");
        }
        // 2. 获取查询条件,构建 Specification 对象, 等价于 MyBatis Plus 的 QueryWrapper
        Specification<Role> spec = (Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // 存储动态条件的集合（类似Wrapper的条件链）
            List<Predicate> predicates = new ArrayList<>();
            // 动态条件1：id不为空时，精确查询（对标wrapper.eq("id", id)）
            if (Objects.nonNull(roleDTO.getId())) {
                predicates.add(cb.equal(root.get("id"), roleDTO.getId()));
            }
            // 动态条件2：roleName不为空时，模糊查询（对标wrapper.like("roleName", roleName)）
            if (StrUtil.isNotEmpty(roleDTO.getName())) {
                predicates.add(cb.like(root.get("roleName"), "%" + roleDTO.getName() + "%"));
            }
            // 动态条件3：roleCode不为空时，模糊查询（对标wrapper.like("roleCode", roleCode)）
            if (StrUtil.isNotEmpty(roleDTO.getCode())) {
                predicates.add(cb.like(root.get("roleCode"), "%" + roleDTO.getCode() + "%"));
            }
            // 动态条件4：roleStatus不为空时，精确查询（对标wrapper.eq("roleStatus", roleStatus)）
            if (Objects.nonNull(roleDTO.getStatus())) {
                predicates.add(cb.equal(root.get("roleStatus"), roleDTO.getStatus()));
            }
            // 将所有条件拼接为AND关系（对标wrapper.and()），也可手动指定OR
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 3. 执行查询
        Page<Role> rolePage = roleRepository.findAll(spec, PageRequest.of(roleDTO.getPageNum() - 1, roleDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "updateTime")));
        // 4. 返回查询结果
        return PageVO.<List<RoleVO>>builder()
                .data(BeanUtil.copyToList(rolePage.getContent(), RoleVO.class))
                .pageNum(rolePage.getNumber() + 1)
                .totalPage(rolePage.getTotalPages())
                .total(rolePage.getTotalElements())
                .build();
    }

    /**
     * 授予角色权限
     *
     * @param rolePermissionConnectionDTOList 需要给角色的权限信息 List 集合, 单个权限信息为 {@link RolePermissionConnectionDTO} 角色权限关联关系信息对象;
     */
    @Transactional(rollbackFor = Exception.class)
    public void empowerPermissions(List<RolePermissionConnectionDTO> rolePermissionConnectionDTOList) {
        // 1. 判断当前账户是否拥有授予角色权限的权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_ROLE_PERMISSION_CONNECTION.getCode())) {
            log.error("当前账户没有添加角色权限的权限!");
            throw new ServiceException("no.create.permission");
        }
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_ROLE_PERMISSION_CONNECTION.getCode())){
            log.error("当前账户没有删除角色权限的权限!");
            throw new ServiceException("no.delete.permission");
        }
        // 2. 判断当前的数据是否存在
        if (CollUtil.isEmpty(rolePermissionConnectionDTOList) || rolePermissionConnectionDTOList.isEmpty()) {
            log.error("不存在需要给角色赋予的权限数据,不予进行角色赋权业务!");
            throw new ServiceException("no.data.need.update");
        }
        // 3. 删除当前需要授予角色的所有权限
        rolePermissionsConnectionService.removeRolePermissionConnectionByRoleId(RolePermissionConnectionDTO.builder().roleIds(rolePermissionConnectionDTOList.stream().map(RolePermissionConnectionDTO::getRoleId).distinct().toList()).build());
        // 4. 添加当前需要授予角色的所有权限
        rolePermissionsConnectionService.addRolePermissionConnection(rolePermissionConnectionDTOList);
    }
}
