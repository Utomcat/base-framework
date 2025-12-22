package com.ranyk.authorization.service.loginAccount;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ranyk.authorization.repository.loginAccount.LoginAccountRepository;
import com.ranyk.common.constant.AccountEnum;
import com.ranyk.common.constant.AccountPermissionEnum;
import com.ranyk.common.constant.AccountStatusEnum;
import com.ranyk.model.business.login.dto.LoginAccountDTO;
import com.ranyk.model.business.login.entity.LoginAccount;
import com.ranyk.model.business.login.vo.LoginAccountVO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CLASS_NAME: LoginAccountService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 系统登录账户业务逻辑处理类
 * @date: 2025-12-18
 */
@Slf4j
@Service
public class LoginAccountService {

    /**
     * 登录账户信息数据库操作类
     */
    private final LoginAccountRepository loginAccountRepository;

    /**
     * 构造方法
     *
     * @param loginAccountRepository 登录账户信息数据库操作类
     */
    @Autowired
    public LoginAccountService(LoginAccountRepository loginAccountRepository) {
        this.loginAccountRepository = loginAccountRepository;
    }

    /**
     * 新增系统登录账户信息
     *
     * @param loginAccountDTO 新增系统登录账户信息封装对象, {@link LoginAccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void addLoginAccount(LoginAccountDTO loginAccountDTO) {
        // 1. 判断当前登录账户是否存在新增系统账户权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_ACCOUNT.getCode())) {
            // 2. 不拥有权限则直接抛出异常
            throw new UserException("no.create.permission");
        }
        // 3. 判断传入的登录的账户名、密码不能为空
        if (StrUtil.isBlank(loginAccountDTO.getUserName())) {
            throw new UserException("user.username.not.blank");
        }
        if (StrUtil.isBlank(loginAccountDTO.getPassword())) {
            throw new UserException("user.password.not.blank");
        }
        // 4. 登录账户名不能已存在
        if (loginAccountRepository.existsByUserName(loginAccountDTO.getUserName())) {
            throw new UserException("user.username.exists");
        }
        // 5. 登录账户默认状态为启用
        LocalDateTime now = LocalDateTime.now();
        LoginAccount saveEntity = LoginAccount.builder()
                .userName(loginAccountDTO.getUserName())
                .password(DigestUtil.md5Hex(loginAccountDTO.getPassword()))
                .accountStatus(AccountStatusEnum.ENABLED.getCode())
                .createTime(now)
                .createId(StpUtil.getLoginIdAsLong())
                .updateTime(now)
                .updateId(StpUtil.getLoginIdAsLong())
                .build();
        // 6. 保存账户信息到数据库
        loginAccountRepository.save(saveEntity);
    }

    /**
     * 注销系统登录账户信息
     *
     * @param loginAccountDTOList 注销系统登录账户信息封装对象 List 集合, 单个参见 {@link LoginAccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void revokedLoginAccount(List<LoginAccountDTO> loginAccountDTOList) {
        // 1. 判断当前登录账户是否存在注销系统账户权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_ACCOUNT.getCode())) {
            throw new UserException("no.delete.permission");
        }
        // 2. 传入的需要注销的账户 id 列表不能为空
        if (Objects.isNull(loginAccountDTOList) || loginAccountDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.delete");
        }
        // 3. 获取需要注销账户的 ID 列表
        List<Long> ids = loginAccountDTOList.stream().map(LoginAccountDTO::getId).toList();
        // 4. 对需要注销的账户进行 超级管理员 排除
        ids = ids.stream().filter(id -> !Objects.equals(AccountEnum.SUPER_ADMIN.getId(), id)).toList();
        // 5. 如果需要更新的账户 id 列表为空则直接抛出异常
        if (ids.isEmpty()) {
            throw new ServiceException("no.data.need.delete");
        }
        // 6. 执行批量注销账户
        int result = loginAccountRepository.batchDeregistrationAccountStatusByIds(ids, AccountStatusEnum.DELETED.getCode());
        log.info("本次注销账户数量: {} 个", result);
    }

    /**
     * 修改系统登录账户信息
     *
     * @param loginAccountDTOList 修改系统登录账户信息封装对象 List 集合, 单个参见 {@link LoginAccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginAccount(List<LoginAccountDTO> loginAccountDTOList) {
        // 1. 判断当前登录账户是否存在修改系统账户权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.UPDATE_ACCOUNT.getCode())) {
            throw new UserException("no.update.permission");
        }
        // 2. 判断是否存在需要修改的账户
        if (Objects.isNull(loginAccountDTOList) || loginAccountDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.update");
        }
        // 3. 对需要进行修改的账户进行 超级管理员 过滤
        loginAccountDTOList = loginAccountDTOList.stream().filter(loginAccountDTO -> !Objects.equals(AccountEnum.SUPER_ADMIN.getId(), loginAccountDTO.getId())).toList();
        // 4. 遍历对应的账户信息,对其先进行是否可修改判断,之后再进行数据修改 TODO 该处可以使用 saveAll 方法批量更新
        loginAccountDTOList.forEach(loginAccountDTO -> {
            // 判断是否存在账户 ID
            if (Objects.isNull(loginAccountDTO.getId())) {
                throw new ServiceException("no.data.need.update");
            }
            // 判断数据库中是否存在指定 ID 的账户数据
            if (!loginAccountRepository.existsById(loginAccountDTO.getId())) {
                throw new ServiceException("no.data.need.update");
            }
            // 判断修改的账户名已经被其他账户使用
            if (loginAccountRepository.existsByUserNameEqualsAndIdNot(loginAccountDTO.getUserName(), loginAccountDTO.getId())) {
                log.error("用户名 {} 已存在", loginAccountDTO.getUserName());
                throw new ServiceException("user.username.exists");
            }
            // 通过账户 ID 查询对应的账户数据,再对其进行数据重新赋值和更新
            loginAccountRepository.findById(loginAccountDTO.getId()).ifPresent(loginAccount -> {
                if (StrUtil.isNotEmpty(loginAccountDTO.getUserName())) {
                    loginAccount.setUserName(loginAccountDTO.getUserName());
                }
                if (StrUtil.isNotEmpty(loginAccountDTO.getPassword())) {
                    loginAccount.setPassword(DigestUtil.md5Hex(loginAccountDTO.getPassword()));
                }
                if (Objects.nonNull(loginAccountDTO.getAccountStatus())) {
                    loginAccount.setAccountStatus(loginAccountDTO.getAccountStatus());
                }
                loginAccount.setUpdateId(StpUtil.getLoginIdAsLong());
                loginAccount.setUpdateTime(LocalDateTime.now());
                loginAccountRepository.save(loginAccount);
            });
        });
    }

    /**
     * 查询系统登录账户信息
     *
     * @param loginAccountDTO 系统登录账户信息查询条件数据封装对象, 参见 {@link LoginAccountDTO}
     * @return 查询结果 List 集合, 单个参见 {@link LoginAccountDTO}
     */
    public PageVO<List<LoginAccountVO>>  queryLoginAccount(LoginAccountDTO loginAccountDTO) {
        // 1. 判断是否存在查询权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.QUERY_ACCOUNT.getCode())) {
            throw new UserException("no.view.permission");
        }
        // 2. 获取查询条件, 构建Specification，等价于MyBatis Plus的QueryWrapper
        Specification<LoginAccount> spec = (Root<LoginAccount> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // 存储动态条件的集合（类似Wrapper的条件链）
            List<Predicate> predicates = new ArrayList<>();
            // 动态条件1：name不为空时，模糊查询（对标wrapper.like("name", name)）
            if (StrUtil.isNotEmpty(loginAccountDTO.getUserName())) {
                predicates.add(cb.like(root.get("userName"), "%" + loginAccountDTO.getUserName() + "%"));
            }
            // 动态条件2：status不为空时，精确查询（对标wrapper.eq("status", status)）
            if (Objects.nonNull(loginAccountDTO.getAccountStatus())) {
                predicates.add(cb.equal(root.get("accountStatus"), loginAccountDTO.getAccountStatus()));
            }
            // 动态条件3：id不为空时，精确查询（对标wrapper.eq("id", id)）
            if (Objects.nonNull(loginAccountDTO.getId())) {
                predicates.add(cb.equal(root.get("id"), loginAccountDTO.getId()));
            }
            // 将所有条件拼接为AND关系（对标wrapper.and()），也可手动指定OR
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 3. 执行查询获取查询结果
        Page<LoginAccount> loginAccountPage = loginAccountRepository.findAll(spec, PageRequest.of(loginAccountDTO.getPageNum(), loginAccountDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "updateTime")));
        // 返回组装结果
        return PageVO.<List<LoginAccountVO>>builder()
                .data(BeanUtil.copyToList(loginAccountPage.getContent(), LoginAccountVO.class))
                .pageNum(loginAccountPage.getNumber() + 1)
                .totalPage(loginAccountPage.getTotalPages())
                .total(loginAccountPage.getTotalElements())
                .build();
    }
}
