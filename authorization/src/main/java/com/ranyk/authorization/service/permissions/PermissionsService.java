package com.ranyk.authorization.service.permissions;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.ranyk.authorization.repository.permissions.PermissionRepository;
import com.ranyk.authorization.service.account.AccountRoleConnectionService;
import com.ranyk.authorization.service.role.RolePermissionsConnectionService;
import com.ranyk.common.constant.AccountPermissionEnum;
import com.ranyk.common.constant.PermissionsStatusEnum;
import com.ranyk.model.business.account.dto.AccountRoleConnectionDTO;
import com.ranyk.model.business.permission.dto.PermissionsDTO;
import com.ranyk.model.business.permission.entity.Permission;
import com.ranyk.model.business.permission.vo.PermissionsVO;
import com.ranyk.model.business.role.dto.RolePermissionConnectionDTO;
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
 * CLASS_NAME: PermissionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限业务逻辑类
 * @date: 2025-12-18
 */
@Slf4j
@Service
public class PermissionsService {
    /**
     * 权限信息数据库操作类对象
     */
    private final PermissionRepository permissionRepository;
    /**
     * 角色权限关联信息业务逻辑类对象
     */
    private final RolePermissionsConnectionService rolePermissionsConnectionService;
    /**
     * 账户角色关联信息业务逻辑类对象
     */
    private final AccountRoleConnectionService accountRoleConnectionService;

    /**
     * 构造函数
     *
     * @param permissionRepository             权限信息数据库操作类对象
     * @param rolePermissionsConnectionService 角色权限关联信息业务逻辑类对象
     * @param accountRoleConnectionService     账户角色关联信息业务逻辑类对象
     */
    @Autowired
    public PermissionsService(PermissionRepository permissionRepository,
                              RolePermissionsConnectionService rolePermissionsConnectionService,
                              AccountRoleConnectionService accountRoleConnectionService) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionsConnectionService = rolePermissionsConnectionService;
        this.accountRoleConnectionService = accountRoleConnectionService;
    }

    /**
     * 通过传入的角色 ID 列表, 获取对应的权限信息 List 集合
     *
     * @param permissionsDTO 传入的需要查询的 角色 ID List 集合数据封装对象,参见 {@link PermissionsDTO#getRoleIds()} 属性
     * @return 返回查询到的权限信息 List 集合, 单个权限信息为 {@link PermissionsDTO} 权限信息对象; 当传入的 roleIds 为 null 时,则返回空权限列表; 当未查询到该账户下的权限信息时,则返回空权限列表;
     */
    public List<PermissionsDTO> getPermissionListByRoleIds(PermissionsDTO permissionsDTO) {
        // 1. 判断传入的 roleIds 是否为 null
        if (Objects.isNull(permissionsDTO.getRoleIds())) {
            log.error("未传入角色 ID List 集合, 不进行权限信息查询逻辑,直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 2. 判断 roleIds 是否没有元素
        if (permissionsDTO.getRoleIds().isEmpty()) {
            log.error("传入的 roleIds 为空, 不进行权限信息查询逻辑,直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 3. 通过传入的 roleIds 获取对应的角色权限关联信息 List 集合
        List<RolePermissionConnectionDTO> rolePermissionConnectionDTOList = rolePermissionsConnectionService.queryRolePermissionConnectionByRoleId(permissionsDTO.getRoleIds());
        // 4. 判断角色权限关联信息 List 集合是否没有元素
        if (rolePermissionConnectionDTOList.isEmpty()) {
            log.error("未查询到该账户下的权限信息, 直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 5. 通过角色权限关联信息 List 集合获取对应的权限 ID List 集合
        List<Long> permissionIds = rolePermissionConnectionDTOList.stream().map(RolePermissionConnectionDTO::getPermissionId).toList();
        // 6. 判断权限 ID List 集合是否没有元素
        if (permissionIds.isEmpty()) {
            log.error("未查询到该账户下的权限信息, 直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 7. 通过权限 ID 列表获取对应的权限信息 List 集合
        List<Permission> permissionList = Optional.of(permissionRepository.findAllById(permissionIds)).orElse(Collections.emptyList());
        // 8. 获取权限信息 List 集合,并将其转换为 PermissionDTO 列表
        return BeanUtil.copyToList(permissionList, PermissionsDTO.class);
    }

    /**
     * 通过 账户Id 查询该账户下的权限信息
     *
     * @param permissionsDTO    需要查询权限信息的 账户 Id 数据封装对象, 参见 {@link PermissionsDTO#getAccountId()} 属性
     * @param isCheckPermission 是否需要检查权限
     * @return 查询到的权限信息 List 集合 {@link PermissionsDTO}
     */
    public List<PermissionsDTO> getPermissionListByAccountIds(PermissionsDTO permissionsDTO, Boolean isCheckPermission) {
        // 1. 判断当前账户是否拥有账户权限查询权限
        if (isCheckPermission && !StpUtil.hasPermission(AccountPermissionEnum.QUERY_PERMISSIONS_INFO.getCode())) {
            log.error("当前账户没有权限查询权限, 不进行权限信息查询逻辑!");
            throw new ServiceException("no.view.permission", AccountPermissionEnum.QUERY_PERMISSIONS_INFO.getCode());
        }
        // 2. 判断账户 Id 是否为 null
        if (Objects.isNull(permissionsDTO.getAccountId())) {
            log.error("未传入账户 Id, 不进行权限信息查询逻辑!");
            throw new ServiceException("no.data.need.query");
        }
        // 3. 通过账户 Id 获取账户角色关联信息 List 集合
        List<AccountRoleConnectionDTO> accountRoleConnectionDTOList = Optional.of(accountRoleConnectionService.queryAccountRoleConnectionByAccountId(permissionsDTO.getAccountId())).orElse(Collections.emptyList());
        // 4. 通过查询出的账户拥有角色 List 查询对应 角色权限关联信息
        List<RolePermissionConnectionDTO> rolePermissionConnectionDTOList = Optional.of(rolePermissionsConnectionService.queryRolePermissionConnectionByRoleId(accountRoleConnectionDTOList.stream().map(AccountRoleConnectionDTO::getRoleId).toList())).orElse(Collections.emptyList());
        // 5. 通过查询的角色权限关联信息 List 获取对应的权限信息
        List<Permission> permissionList = Optional.of(permissionRepository.findAllById(rolePermissionConnectionDTOList.stream().map(RolePermissionConnectionDTO::getPermissionId).toList())).orElse(Collections.emptyList());
        // 6. 获取权限信息 List 集合,并将其转换为 PermissionDTO 列表
        return BeanUtil.copyToList(permissionList, PermissionsDTO.class);
    }

    /**
     * 新增权限信息
     *
     * @param permissionsDTOList 新增权限信息数据封装对象 List 集合, 单个权限信息为 {@link PermissionsDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(List<PermissionsDTO> permissionsDTOList) {
        // 1. 判断当前用户是否存在对应的 权限新增权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_PERMISSIONS_INFO.getCode())) {
            throw new ServiceException("no.create.permission");
        }
        // 2. 判断是否存在权限数据
        if (Objects.isNull(permissionsDTOList) || permissionsDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.create");
        }
        // 3. 判断当前新增的权限 code 是否当前系统已经存在
        if (permissionRepository.existsByCodeIn(permissionsDTOList.stream().map(PermissionsDTO::getCode).toList())) {
            throw new ServiceException("duplicate.data.found", permissionsDTOList.stream().map(PermissionsDTO::getCode).toList());
        }
        // 4. 获取当前用户 ID
        long loginId = StpUtil.getLoginIdAsLong();
        // 5. 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 6. 遍历权限数据 List 集合,组装对应的权限信息保存 List 集合
        List<Permission> permissionList = permissionsDTOList.stream().map(permissionsDTO -> {
            Permission permission = BeanUtil.copyProperties(permissionsDTO, Permission.class);
            permission.setId(null);
            permission.setCreateId(loginId);
            permission.setUpdateId(loginId);
            permission.setCreateTime(now);
            permission.setUpdateTime(now);
            permission.setStatus(PermissionsStatusEnum.NORMAL.getCode());
            return permission;
        }).toList();
        // 7. 保存权限信息
        List<Permission> permissions = permissionRepository.saveAllAndFlush(permissionList);
        // 8. 判断是否保存一致
        if (!Objects.equals(permissions.size(), permissionsDTOList.size())) {
            log.error("新增权限信息失败, 新增权限数量为: {}", permissions.size());
            throw new ServiceException("create.permission.fail");
        }
        // 9. 输出日志
        log.info("新增权限信息成功, 新增权限数量为: {}", permissions.size());
    }

    /**
     * 删除权限信息
     *
     * @param permissionsDTO 删除权限信息数据封装对象 {@link PermissionsDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(PermissionsDTO permissionsDTO) {
        // 1. 判断当前用户是否存在的对应的 删除权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_PERMISSIONS_INFO.getCode())) {
            throw new ServiceException("no.delete.permission");
        }
        // 2. 判断是否存在需要删除的数据
        if (Objects.isNull(permissionsDTO.getIds()) || permissionsDTO.getIds().isEmpty()) {
            throw new ServiceException("no.data.need.delete");
        }
        // 3. 执行对应的更新语句
        int updateCount = permissionRepository.updatePermissionStatusByIdIn(permissionsDTO.getIds(), PermissionsStatusEnum.DELETE.getCode(), StpUtil.getLoginIdAsLong(), LocalDateTime.now());
        // 4. 输出日志
        log.info("删除权限信息成功, 删除权限数量为: {}", updateCount);
    }

    /**
     * 修改权限信息
     *
     * @param permissionsDTOList 修改权限信息数据封装对象 List 集合, 单个权限信息为 {@link PermissionsDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(List<PermissionsDTO> permissionsDTOList) {
        // 1. 判断当前用户是否存在对应的 修改权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.UPDATE_PERMISSIONS_INFO.getCode())) {
            throw new ServiceException("no.update.permission");
        }
        // 2. 判断是否存在需要修改的数据
        if (Objects.isNull(permissionsDTOList) || permissionsDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.update");
        }
        // 3. 判断数据是否完整: 权限数据 ID 不能为空; 权限名称、权限代码、权限状态不能同时为空
        permissionsDTOList.forEach(permissionsDTO -> {
            // 判断权限数据 ID 是否为空
            if (Objects.isNull(permissionsDTO.getId())) {
                throw new ServiceException("data.incomplete");
            } else {
                // 权限名称、权限代码、权限状态不能同时为空
                if (StrUtil.isBlank(permissionsDTO.getName())
                        && StrUtil.isBlank(permissionsDTO.getCode())
                        && Objects.isNull(permissionsDTO.getStatus())) {
                    throw new ServiceException("data.incomplete");
                }
            }
        });
        // 4. 判断是否已经存在指定的权限代码存在,但是数据 ID 不是当前的数据 ID
        List<String> permissionCodeList = permissionsDTOList.stream().map(PermissionsDTO::getCode).filter(StrUtil::isNotBlank).toList();
        List<Long> idList = permissionsDTOList.stream().map(PermissionsDTO::getId).filter(Objects::nonNull).toList();
        if (permissionRepository.existsByCodeInAndIdNotIn(permissionCodeList, idList)) {
            throw new ServiceException("duplicate.data.found");
        }
        // 5. 获取当前用户 ID
        long loginId = StpUtil.getLoginIdAsLong();
        // 6. 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 7. 获取对应的权限数据
        Map<Long, PermissionsDTO> needSavePermissionMap = permissionsDTOList.stream().filter(permissionsDTO -> Objects.nonNull(permissionsDTO.getId())).collect(Collectors.toMap(
                PermissionsDTO::getId,
                permissionsDTO -> permissionsDTO,
                (existing, replacement) -> existing
        ));
        List<Permission> permissionList = Optional.of(permissionRepository.findAllById(idList)).orElse(Collections.emptyList());
        // 8. 遍历对应的权限数据 List 集合, 组装对应的权限信息
        permissionList.forEach(permission -> {
            PermissionsDTO permissionsDTO = needSavePermissionMap.get(permission.getId());
            if (StrUtil.isNotBlank(permissionsDTO.getName())) {
                permission.setName(permissionsDTO.getName());
            }
            if (StrUtil.isNotBlank(permissionsDTO.getCode())) {
                permission.setCode(permissionsDTO.getCode());
            }
            if (Objects.nonNull(permissionsDTO.getStatus())) {
                permission.setStatus(permissionsDTO.getStatus());
            }
            permission.setDesc(permissionsDTO.getDesc());
            permission.setRemark(permissionsDTO.getRemark());
            permission.setUpdateId(loginId);
            permission.setUpdateTime(now);
        });
        // 9. 执行数据更新
        List<Permission> permissions = permissionRepository.saveAll(permissionList);
        // 10. 判断是否将其对应的数据修改成功
        if (!Objects.equals(permissions.size(), permissionsDTOList.size())) {
            log.error("修改权限信息失败,需要修改的权限数量为 {} 实际修改权限数量为 {}", permissionsDTOList.size(), permissions.size());
            throw new ServiceException("update.data.fail");
        }
        // 11. 输出日志
        log.info("修改权限信息成功, 修改权限数量为: {}", permissions.size());
    }

    /**
     * 查询权限信息
     *
     * @param permissionsDTO 查询权限信息数据封装对象, 单个参见 {@link PermissionsDTO}
     * @return 查询权限结果 分页查询结果对象 {@link PageVO}, 单个权限数据参见 {@link PermissionsVO}
     */
    public PageVO<List<PermissionsVO>> queryPermissions(PermissionsDTO permissionsDTO) {
        // 1. 判断当前用户是否具有权限查询权限信息
        if (!StpUtil.hasPermission(AccountPermissionEnum.QUERY_PERMISSIONS_INFO.getCode())) {
            throw new ServiceException("no.view.permission", AccountPermissionEnum.QUERY_PERMISSIONS_INFO.getCode());
        }
        // 2. 获取查询条件,构建 Specification 对象, 等价于 MyBatis Plus 的 QueryWrapper
        Specification<Permission> spec = (Root<Permission> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // 存储动态条件的集合（类似Wrapper的条件链）
            List<Predicate> predicates = new ArrayList<>();
            // 动态条件1：id不为空时，精确查询（对标wrapper.eq("id", id)）
            if (Objects.nonNull(permissionsDTO.getId())) {
                predicates.add(cb.equal(root.get("id"), permissionsDTO.getId()));
            }
            // 动态条件2：name 不为空时，模糊查询（对标wrapper.like("name", name)）
            if (StrUtil.isNotBlank(permissionsDTO.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + permissionsDTO.getName() + "%"));
            }
            // 动态条件3：code 不为空时，模糊查询（对标wrapper.like("code", code)）
            if (StrUtil.isNotBlank(permissionsDTO.getCode())) {
                predicates.add(cb.like(root.get("code"), "%" + permissionsDTO.getCode() + "%"));
            }
            // 动态条件4：status 不为空时，精确查询（对标wrapper.eq("status", status)）
            if (Objects.nonNull(permissionsDTO.getStatus())) {
                predicates.add(cb.equal(root.get("status"), permissionsDTO.getStatus()));
            }
            // 动态条件5：remark 不为空时，模糊查询（对标wrapper.like("remark", remark)）
            if (StrUtil.isNotBlank(permissionsDTO.getRemark())){
                predicates.add(cb.like(root.get("remark"), "%" + permissionsDTO.getRemark() + "%"));
            }
            // 动态条件6：desc 不为空时，模糊查询（对标wrapper.like("desc", desc)）
            if (StrUtil.isNotBlank(permissionsDTO.getDesc())){
                predicates.add(cb.like(root.get("desc"), "%" + permissionsDTO.getDesc() + "%"));
            }
            // 动态条件7：type 不为空时，精确查询（对标wrapper.eq("type", type)）
            if (Objects.nonNull(permissionsDTO.getType())){
                predicates.add(cb.equal(root.get("type"), permissionsDTO.getType()));
            }
            // 将所有条件拼接为AND关系（对标wrapper.and()），也可手动指定OR
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 3. 执行查询
        Page<Permission> permissionPagepage = permissionRepository.findAll(spec, PageRequest.of(permissionsDTO.getPageNum() - 1, permissionsDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "updateTime")));
        // 4. 返回查询结果
        return PageVO.<List<PermissionsVO>>builder()
                .data(BeanUtil.copyToList(permissionPagepage.getContent(), PermissionsVO.class))
                .pageNum(permissionPagepage.getNumber() + 1)
                .totalPage(permissionPagepage.getTotalPages())
                .total(permissionPagepage.getTotalElements())
                .build();
    }

    /**
     * 获取当前登录用户权限信息
     *
     * @return 当前登录用户权限信息 List 集合, 单个权限数据参见 {@link PermissionsDTO}
     */
    public List<PermissionsDTO> getCurrentUserOfPermissions() {
        // 1. 获取当前登录用户ID
        Long loginId = StpUtil.getLoginIdAsLong();
        return this.getPermissionListByAccountIds(PermissionsDTO.builder().accountId(loginId).build(), Boolean.TRUE);
    }
}
