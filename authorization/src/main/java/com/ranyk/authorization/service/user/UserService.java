package com.ranyk.authorization.service.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.ranyk.authorization.repository.user.UserBaseRepository;
import com.ranyk.authorization.service.account.AccountUserConnectionService;
import com.ranyk.common.constant.AccountPermissionEnum;
import com.ranyk.common.constant.SymbolEnum;
import com.ranyk.common.constant.UserStatusEnum;
import com.ranyk.model.business.account.dto.AccountUserConnectionDTO;
import com.ranyk.model.business.userinfo.dto.UserBaseDTO;
import com.ranyk.model.business.userinfo.entity.UserBase;
import com.ranyk.model.business.userinfo.vo.UserBaseVO;
import com.ranyk.model.exception.service.ServiceException;
import com.ranyk.model.exception.user.UserException;
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
 * CLASS_NAME: UserService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户业务逻辑处理类
 * @date: 2025-12-18
 */
@Slf4j
@Service
public class UserService {

    /**
     * 用户基本信息数据库操作类对象
     */
    private final UserBaseRepository userBaseRepository;
    /**
     * 账户用户关联关系业务逻辑类对象
     */
    private final AccountUserConnectionService accountUserConnectionService;


    /**
     * 构造方法
     *
     * @param userBaseRepository           用户基本信息数据库操作类对象
     * @param accountUserConnectionService 账户用户关联关系业务逻辑类对象
     */
    @Autowired
    public UserService(UserBaseRepository userBaseRepository,
                       AccountUserConnectionService accountUserConnectionService) {
        this.userBaseRepository = userBaseRepository;
        this.accountUserConnectionService = accountUserConnectionService;
    }

    /**
     * 新增用户, 批量新增
     *
     * @param userBaseDTOList 新增用户信息 List 集合,单个数据参见 {@link UserBaseDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public List<UserBaseDTO> addUser(List<UserBaseDTO> userBaseDTOList) {
        // 1. 判断当前用户是否有权限新增用户信息
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_USER_INFO.getCode())) {
            throw new UserException("no.create.permission");
        }
        // 2. 判断用户数据是否为空
        if (Objects.isNull(userBaseDTOList) || userBaseDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.create");
        }
        // 3. 获取当前登录用户的用户 ID
        long loginId = StpUtil.getLoginIdAsLong();
        // 4. 获取当前的时间
        LocalDateTime now = LocalDateTime.now();
        // 5. 批量处理用户数据 => 对没有值的数据进行默认值填充,同时设置数据的公共属性(数据创建人、数据创建时间、数据更新人、数据更新时间)
        userBaseDTOList.forEach(userBaseDTO -> {
            if (Objects.equals(userBaseDTO.getId(), 0L)){
                userBaseDTO.setId(null);
            }
            // 当录入的 姓氏 和 名字 均为空, 则设置用户全名为 -
            if (StrUtil.isBlank(userBaseDTO.getFirstName()) && StrUtil.isBlank(userBaseDTO.getLastName())) {
                userBaseDTO.setUserName(SymbolEnum.DASH_EN.getCode());
            } else {
                // 当未传入用户的全名,则自动设置用户全名为 用户姓氏 + 用户名字
                if (StrUtil.isBlank(userBaseDTO.getUserName())){
                    userBaseDTO.setUserName(userBaseDTO.getLastName() + userBaseDTO.getFirstName());
                }
            }
            if (StrUtil.isBlank(userBaseDTO.getFirstName())) {
                userBaseDTO.setFirstName(SymbolEnum.DASH_EN.getCode());
            }
            if (StrUtil.isBlank(userBaseDTO.getLastName())) {
                userBaseDTO.setLastName(SymbolEnum.DASH_EN.getCode());
            }
            if (Objects.isNull(userBaseDTO.getSex())) {
                userBaseDTO.setSex(0);
            }
            if (StrUtil.isBlank(userBaseDTO.getNickName())) {
                userBaseDTO.setNickName(SymbolEnum.DASH_EN.getCode());
            }
            if (StrUtil.isBlank(userBaseDTO.getFixedLinePhone())) {
                userBaseDTO.setFixedLinePhone(SymbolEnum.DASH_EN.getCode());
            }
            if (StrUtil.isBlank(userBaseDTO.getPhone())) {
                userBaseDTO.setPhone(SymbolEnum.DASH_EN.getCode());
            }
            if (StrUtil.isBlank(userBaseDTO.getEmail())) {
                userBaseDTO.setEmail(SymbolEnum.DASH_EN.getCode());
            }
            if (Objects.isNull(userBaseDTO.getStatus())) {
                userBaseDTO.setStatus(1);
            }
            userBaseDTO.setCreateId(loginId);
            userBaseDTO.setUpdateId(loginId);
            userBaseDTO.setCreateTime(now);
            userBaseDTO.setUpdateTime(now);
        });
        // 6. 批量保存用户数据
        List<UserBase> userBaseList = userBaseRepository.saveAll(BeanUtil.copyToList(userBaseDTOList, UserBase.class));
        // 7. 判断保存的用户数据数量是否与需要保存的用户数据数量一致
        if (userBaseList.size() != userBaseDTOList.size()) {
            log.error("新增用户失败,需要新增的用户数据为 {} 实际新增数据为 {}", userBaseDTOList.size(), userBaseList.size());
            throw new ServiceException("create.data.fail");
        }
        // 8. 输出保存成功日志
        log.info("新增用户成功,新增用户数据为 {}", userBaseList.size());
        return BeanUtil.copyToList(userBaseList, UserBaseDTO.class);
    }

    /**
     * 删除用户, 批量删除
     *
     * @param userBaseDTO 删除用户信息,参见 {@link UserBaseDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(UserBaseDTO userBaseDTO) {
        // 1. 判断当前用户是否有权限删除用户信息
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_USER_INFO.getCode())) {
            throw new UserException("no.delete.permission");
        }
        // 2. 判断是否存在需要删除的用户数据
        if (Objects.isNull(userBaseDTO) || Objects.isNull(userBaseDTO.getIds()) || userBaseDTO.getIds().isEmpty()) {
            throw new ServiceException("no.data.need.delete");
        }
        // 3. 批量删除用户数据
        int i = userBaseRepository.batchUpdateUserStatusByIds(userBaseDTO.getIds(), UserStatusEnum.DELETE.getCode(), StpUtil.getLoginIdAsLong(), LocalDateTime.now());
        // 4. 输出删除成功日志
        log.info("删除用户成功,删除用户数据为 {} 条", i);
    }

    /**
     * 修改用户, 批量修改
     *
     * @param userBaseDTOList 修改用户信息 List 集合,参见 {@link UserBaseDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(List<UserBaseDTO> userBaseDTOList) {
        // 1. 判断当前用户是否有权限修改用户信息
        if (!StpUtil.hasPermission(AccountPermissionEnum.UPDATE_USER_INFO.getCode())) {
            throw new UserException("no.update.permission");
        }
        // 2. 判断是否存在需要编辑的用户数据
        if (Objects.isNull(userBaseDTOList) || userBaseDTOList.isEmpty() || userBaseDTOList.stream().allMatch(Objects::isNull)) {
            throw new ServiceException("no.data.need.update");
        }
        // 3. 获取当前登录用户的用户 ID
        long loginId = StpUtil.getLoginIdAsLong();
        // 4. 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 5. 获取当前所有数据的 id
        List<Long> ids = userBaseDTOList.stream().map(UserBaseDTO::getId).collect(Collectors.toList());
        // 6. 将传入的入参转换为 Map 集合, key 为 id, value 为 UserBaseDTO 对象
        Map<Long, UserBaseDTO> userBaseDTOMap = userBaseDTOList.stream().collect(Collectors.toMap(UserBaseDTO::getId, userBaseDTO -> userBaseDTO));
        // 7. 批量查询用户数据
        List<UserBase> userBaseList = userBaseRepository.findAllById(ids);
        // 8. 遍历用户数据, 进行数据填充
        userBaseList.forEach(userBase -> {
            UserBaseDTO userBaseDTO = Optional.of(userBaseDTOMap.get(userBase.getId())).orElse(UserBaseDTO.builder().build());
            if (StrUtil.isNotBlank(userBaseDTO.getLastName()) || StrUtil.isNotBlank(userBaseDTO.getFirstName())) {
                userBase.setUserName((StrUtil.isNotBlank(userBaseDTO.getLastName()) ? userBaseDTO.getLastName() : userBase.getLastName()) + (StrUtil.isNotBlank(userBaseDTO.getFirstName()) ? userBaseDTO.getFirstName() : userBase.getFirstName()));
            }
            if (StrUtil.isNotBlank(userBaseDTO.getFirstName())) {
                userBase.setFirstName(userBaseDTO.getFirstName());
            }
            if (StrUtil.isNotBlank(userBaseDTO.getLastName())) {
                userBase.setLastName(userBaseDTO.getLastName());
            }
            if (Objects.nonNull(userBaseDTO.getSex())) {
                userBase.setSex(userBaseDTO.getSex());
            }
            if (StrUtil.isNotBlank(userBaseDTO.getNickName())) {
                userBase.setNickName(userBaseDTO.getNickName());
            }
            if (StrUtil.isNotBlank(userBaseDTO.getFixedLinePhone())) {
                userBase.setFixedLinePhone(userBaseDTO.getFixedLinePhone());
            }
            if (StrUtil.isNotBlank(userBaseDTO.getPhone())) {
                userBase.setPhone(userBaseDTO.getPhone());
            }
            if (StrUtil.isNotBlank(userBaseDTO.getEmail())) {
                userBase.setEmail(userBaseDTO.getEmail());
            }
            if (Objects.nonNull(userBaseDTO.getStatus())) {
                userBase.setStatus(userBaseDTO.getStatus());
            }
            userBase.setUpdateId(loginId);
            userBase.setUpdateTime(now);
        });
        // 9. 批量保存用户数据
        List<UserBase> userBases = userBaseRepository.saveAllAndFlush(userBaseList);
        // 10. 判断保存的用户数据数量是否与需要保存的用户数据数量一致
        if (userBases.size() != userBaseDTOList.size()) {
            log.error("修改用户失败,需要修改的用户数据为 {} 实际修改数据为 {}", userBaseDTOList.size(), userBases.size());
            throw new ServiceException("update.data.fail");
        }
        // 11. 输出修改成功日志
        log.info("修改用户成功,修改用户数据为 {} 条", userBases.size());
    }

    /**
     * 查询用户, 指定查询条件
     *
     * @param userBaseDTO 查询用户信息条件数据封装对象,参见 {@link UserBaseDTO}
     * @return 查询用户结果, 参见 {@link UserBaseVO}
     */
    public PageVO<List<UserBaseVO>> queryUser(UserBaseDTO userBaseDTO) {
        // 1. 判断当前用户是否有权限查询用户信息
        if (!StpUtil.hasPermission(AccountPermissionEnum.QUERY_USER_INFO.getCode())) {
            throw new UserException("no.view.permission");
        }
        // 2. 获取查询条件,构建 Specification 对象, 等价于 MyBatis Plus 的 QueryWrapper
        Specification<UserBase> spec = (Root<UserBase> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // 存储动态条件的集合（类似Wrapper的条件链）
            List<Predicate> predicates = new ArrayList<>();
            // 动态条件1：userName不为空时，模糊查询（对标wrapper.like("userName", userName)）
            if (StrUtil.isNotEmpty(userBaseDTO.getUserName())) {
                predicates.add(cb.like(root.get("userName"), "%" + userBaseDTO.getUserName() + "%"));
            }
            // 动态条件2：firstName不为空时，模糊查询（对标wrapper.like("firstName", firstName)）
            if (StrUtil.isNotEmpty(userBaseDTO.getFirstName())) {
                predicates.add(cb.like(root.get("firstName"), "%" + userBaseDTO.getFirstName() + "%"));
            }
            // 动态条件3：lastName不为空时，模糊查询（对标wrapper.like("lastName", lastName)）
            if (StrUtil.isNotEmpty(userBaseDTO.getLastName())) {
                predicates.add(cb.like(root.get("lastName"), "%" + userBaseDTO.getLastName() + "%"));
            }
            // 动态条件4：nickName不为空时，模糊查询（对标wrapper.like("nickName", nickName)）
            if (StrUtil.isNotEmpty(userBaseDTO.getNickName())) {
                predicates.add(cb.like(root.get("nickName"), "%" + userBaseDTO.getNickName() + "%"));
            }
            // 动态条件5：sex不为空时，精确查询（对标wrapper.eq("sex", sex)）
            if (Objects.nonNull(userBaseDTO.getSex()) && userBaseDTO.getSex() >= 0) {
                predicates.add(cb.equal(root.get("sex"), userBaseDTO.getSex()));
            }
            // 动态条件6：fixedLinePhone不为空时，模糊查询（对标wrapper.like("fixedLinePhone", fixedLinePhone)）
            if (StrUtil.isNotEmpty(userBaseDTO.getFixedLinePhone())) {
                predicates.add(cb.like(root.get("fixedLinePhone"), "%" + userBaseDTO.getFixedLinePhone() + "%"));
            }
            // 动态条件7：phone不为空时，模糊查询（对标wrapper.like("phone", phone)）
            if (StrUtil.isNotEmpty(userBaseDTO.getPhone())) {
                predicates.add(cb.like(root.get("phone"), "%" + userBaseDTO.getPhone() + "%"));
            }
            // 动态条件8：email不为空时，模糊查询（对标wrapper.like("email", email)）
            if (StrUtil.isNotEmpty(userBaseDTO.getEmail())) {
                predicates.add(cb.like(root.get("email"), "%" + userBaseDTO.getEmail() + "%"));
            }
            // 动态条件9：status不为空时，精确查询（对标wrapper.eq("status", status)）
            if (Objects.nonNull(userBaseDTO.getStatus()) && userBaseDTO.getStatus() > -2) {
                predicates.add(cb.equal(root.get("status"), userBaseDTO.getStatus()));
            }
            // 动态条件10：id不为空时，精确查询（对标wrapper.eq("id", id)）
            if (Objects.nonNull(userBaseDTO.getId())) {
                predicates.add(cb.equal(root.get("id"), userBaseDTO.getId()));
            }
            // 将所有条件拼接为AND关系（对标wrapper.and()），也可手动指定OR
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 3. 执行查询获取查询结果
        Page<UserBase> userBasePage = userBaseRepository.findAll(spec, PageRequest.of(userBaseDTO.getPageNum() - 1, userBaseDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "updateTime")));
        // 4. 构建查询结果返回
        return PageVO.<List<UserBaseVO>>builder()
                .data(BeanUtil.copyToList(userBasePage.getContent(), UserBaseVO.class))
                .pageNum(userBasePage.getNumber() + 1)
                .totalPage(userBasePage.getTotalPages())
                .total(userBasePage.getTotalElements())
                .build();
    }

    /**
     * 通过指定用户 ID 查询对应的用户信息
     *
     * @param userBaseDTO 查询用户信息条件数据封装对象,参见 {@link UserBaseDTO#getId()} 属性
     * @return 查询用户信息结果, 参见 {@link UserBaseDTO}
     */
    public UserBaseDTO queryUserInfoById(UserBaseDTO userBaseDTO) {
        if (Objects.isNull(userBaseDTO.getId())) {
            log.error("查询用户信息失败,用户 ID 为空!");
            throw new UserException("data.incomplete");
        }
        UserBase userBase = userBaseRepository.findById(userBaseDTO.getId()).orElse(UserBase.builder().build());
        return BeanUtil.copyProperties(userBase, UserBaseDTO.class);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息, 参见 {@link UserBaseDTO}
     */
    public UserBaseDTO getCurrentUser() {
        // 1. 获取当前登录账户 ID
        Long accountId = StpUtil.getLoginIdAsLong();
        // 2. 通过当前登录账户 ID 查询当前账户和用户关联信息
        AccountUserConnectionDTO accountUserConnectionDTO = accountUserConnectionService.queryUserInfoIdByAccountId(AccountUserConnectionDTO.builder().accountId(accountId).build());
        // 3. 通过用户 ID 获取用户信息
        if (Objects.isNull(accountUserConnectionDTO.getUserId())) {
            return UserBaseDTO.builder().build();
        }
        // 4. 通过用户 ID 获取用户信息
        return queryUserInfoById(UserBaseDTO.builder().id(accountUserConnectionDTO.getUserId()).build());
    }
}
