package com.ranyk.authorization.service.account;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ranyk.authorization.repository.account.AccountRepository;
import com.ranyk.common.constant.AccountEnum;
import com.ranyk.common.constant.AccountPermissionEnum;
import com.ranyk.common.constant.AccountStatusEnum;
import com.ranyk.model.business.account.dto.AccountDTO;
import com.ranyk.model.business.account.dto.AccountRoleConnectionDTO;
import com.ranyk.model.business.account.dto.AccountUserConnectionDTO;
import com.ranyk.model.business.account.entity.Account;
import com.ranyk.model.business.account.vo.AccountVO;
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
public class AccountService {

    /**
     * 账户角色关联关系业务逻辑类对象
     */
    private final AccountRoleConnectionService accountRoleConnectionService;
    /**
     * 账户用户关联关系业务逻辑类对象
     */
    private final AccountUserConnectionService accountUserConnectionService;
    /**
     * 登录账户信息数据库操作类
     */
    private final AccountRepository accountRepository;

    /**
     * 构造方法
     *
     * @param accountRoleConnectionService 账户角色关联关系业务逻辑类对象
     * @param accountUserConnectionService 账户用户关联关系业务逻辑类对象
     * @param accountRepository            登录账户信息数据库操作类
     */
    @Autowired
    public AccountService(AccountRoleConnectionService accountRoleConnectionService,
                          AccountUserConnectionService accountUserConnectionService,
                          AccountRepository accountRepository) {
        this.accountRoleConnectionService = accountRoleConnectionService;
        this.accountUserConnectionService = accountUserConnectionService;
        this.accountRepository = accountRepository;
    }

    /**
     * 新增系统登录账户信息
     *
     * @param accountDTO 新增系统登录账户信息封装对象, {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void addLoginAccount(AccountDTO accountDTO) {
        // 1. 判断当前登录账户是否存在新增系统账户权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_ACCOUNT.getCode())) {
            // 2. 不拥有权限则直接抛出异常
            throw new UserException("no.create.permission");
        }
        // 3. 判断传入的登录的账户名、密码不能为空
        if (StrUtil.isBlank(accountDTO.getUserName())) {
            throw new UserException("user.username.not.blank");
        }
        if (StrUtil.isBlank(accountDTO.getPassword())) {
            throw new UserException("user.password.not.blank");
        }
        // 4. 登录账户名不能已存在
        if (accountRepository.existsByUserName(accountDTO.getUserName())) {
            throw new UserException("user.username.exists");
        }
        // 5. 登录账户默认状态为启用
        LocalDateTime now = LocalDateTime.now();
        Account saveEntity = Account.builder()
                .userName(accountDTO.getUserName())
                .password(DigestUtil.md5Hex(accountDTO.getPassword()))
                .accountStatus(AccountStatusEnum.ENABLED.getCode())
                .createTime(now)
                .createId(StpUtil.getLoginIdAsLong())
                .updateTime(now)
                .updateId(StpUtil.getLoginIdAsLong())
                .build();
        // 6. 保存账户信息到数据库
        accountRepository.save(saveEntity);
    }

    /**
     * 注销系统登录账户信息
     *
     * @param accountDTOList 注销系统登录账户信息封装对象 List 集合, 单个参见 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void revokedLoginAccount(List<AccountDTO> accountDTOList) {
        // 1. 判断当前登录账户是否存在注销系统账户权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_ACCOUNT.getCode())) {
            throw new UserException("no.delete.permission");
        }
        // 2. 传入的需要注销的账户 id 列表不能为空
        if (Objects.isNull(accountDTOList) || accountDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.delete");
        }
        // 3. 获取需要注销账户的 ID 列表
        List<Long> ids = accountDTOList.stream().map(AccountDTO::getId).toList();
        // 4. 对需要注销的账户进行 超级管理员 排除
        ids = ids.stream().filter(id -> !Objects.equals(AccountEnum.SUPER_ADMIN.getId(), id)).toList();
        // 5. 如果需要更新的账户 id 列表为空则直接抛出异常
        if (ids.isEmpty()) {
            throw new ServiceException("no.data.need.delete");
        }
        // 6. 执行批量注销账户
        int result = accountRepository.batchDeregistrationAccountStatusByIds(ids, AccountStatusEnum.DELETED.getCode(), StpUtil.getLoginIdAsLong(), LocalDateTime.now());
        log.info("本次注销账户数量: {} 个", result);
    }

    /**
     * 修改系统登录账户信息
     *
     * @param accountDTOList 修改系统登录账户信息封装对象 List 集合, 单个参见 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginAccount(List<AccountDTO> accountDTOList) {
        // 1. 判断当前登录账户是否存在修改系统账户权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.UPDATE_ACCOUNT.getCode())) {
            throw new UserException("no.update.permission");
        }
        // 2. 判断是否存在需要修改的账户
        if (Objects.isNull(accountDTOList) || accountDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.update");
        }
        // 3. 对需要进行修改的账户进行 超级管理员 过滤
        accountDTOList = accountDTOList.stream().filter(accountDTO -> !Objects.equals(AccountEnum.SUPER_ADMIN.getId(), accountDTO.getId())).toList();
        // 4. 遍历对应的账户信息,对其先进行是否可修改判断,之后再进行数据修改 TODO 该处可以使用 saveAll 方法批量更新
        accountDTOList.forEach(accountDTO -> {
            // 判断是否存在账户 ID
            if (Objects.isNull(accountDTO.getId())) {
                throw new ServiceException("no.data.need.update");
            }
            // 判断数据库中是否存在指定 ID 的账户数据
            if (!accountRepository.existsById(accountDTO.getId())) {
                throw new ServiceException("no.data.need.update");
            }
            // 判断修改的账户名已经被其他账户使用
            if (accountRepository.existsByUserNameEqualsAndIdNot(accountDTO.getUserName(), accountDTO.getId())) {
                log.error("用户名 {} 已存在", accountDTO.getUserName());
                throw new ServiceException("user.username.exists");
            }
            // 通过账户 ID 查询对应的账户数据,再对其进行数据重新赋值和更新
            accountRepository.findById(accountDTO.getId()).ifPresent(account -> {
                if (StrUtil.isNotEmpty(accountDTO.getUserName())) {
                    account.setUserName(accountDTO.getUserName());
                }
                if (StrUtil.isNotEmpty(accountDTO.getPassword())) {
                    account.setPassword(DigestUtil.md5Hex(accountDTO.getPassword()));
                }
                if (Objects.nonNull(accountDTO.getAccountStatus())) {
                    account.setAccountStatus(accountDTO.getAccountStatus());
                }
                account.setUpdateId(StpUtil.getLoginIdAsLong());
                account.setUpdateTime(LocalDateTime.now());
                accountRepository.save(account);
            });
        });
    }

    /**
     * 查询系统登录账户信息列表 - 分页查询
     *
     * @param accountDTO 系统登录账户信息查询条件数据封装对象, 参见 {@link AccountDTO}
     * @return 查询结果 List 集合, 单个参见 {@link AccountVO}
     */
    public PageVO<List<AccountVO>> queryLoginAccount(AccountDTO accountDTO) {
        // 1. 判断是否存在查询权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.QUERY_ACCOUNT.getCode())) {
            throw new UserException("no.view.permission");
        }
        // 2. 获取查询条件, 构建Specification，等价于MyBatis Plus的QueryWrapper
        Specification<Account> spec = (Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            // 存储动态条件的集合（类似Wrapper的条件链）
            List<Predicate> predicates = new ArrayList<>();
            // 动态条件1：name不为空时，模糊查询（对标wrapper.like("name", name)）
            if (StrUtil.isNotEmpty(accountDTO.getUserName())) {
                predicates.add(cb.like(root.get("userName"), "%" + accountDTO.getUserName() + "%"));
            }
            // 动态条件2：status不为空时，精确查询（对标wrapper.eq("status", status)）
            if (Objects.nonNull(accountDTO.getAccountStatus())) {
                predicates.add(cb.equal(root.get("accountStatus"), accountDTO.getAccountStatus()));
            }
            // 动态条件3：id不为空时，精确查询（对标wrapper.eq("id", id)）
            if (Objects.nonNull(accountDTO.getId())) {
                predicates.add(cb.equal(root.get("id"), accountDTO.getId()));
            }
            // 将所有条件拼接为AND关系（对标wrapper.and()），也可手动指定OR
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 3. 执行查询获取查询结果
        Page<Account> loginAccountPage = accountRepository.findAll(spec, PageRequest.of(accountDTO.getPageNum() - 1, accountDTO.getPageSize(), Sort.by(Sort.Direction.DESC, "updateTime")));
        // 返回组装结果
        return PageVO.<List<AccountVO>>builder()
                .data(BeanUtil.copyToList(loginAccountPage.getContent(), AccountVO.class))
                .pageNum(loginAccountPage.getNumber() + 1)
                .totalPage(loginAccountPage.getTotalPages())
                .total(loginAccountPage.getTotalElements())
                .build();
    }

    /**
     * 通过用户名和密码查询系统登录账户信息
     *
     * @param accountDTO 用户登录参数封装对象, 参见 {@link AccountDTO}
     * @return 返回查询到的系统内登录账户信息对象 {@link AccountDTO}, 注意该返回对象和传入的入参对象是不同的,因此是两个不同的对象
     */
    public AccountDTO queryLoginAccountByUserNameAndPassword(AccountDTO accountDTO) {
        // 通过传入的用户名和密码进行账户对象的查询, 当未查询到时, 返回一个新建的 账户(Account) 对象, 此对象没有数据 id
        Account account = accountRepository.findByUserNameAndPasswordAndAccountStatusEquals(accountDTO.getUserName(), DigestUtil.md5Hex(accountDTO.getPassword()), AccountStatusEnum.ENABLED.getCode()).orElse(Account.builder().build());
        // 返回查询结果
        return BeanUtil.copyProperties(account, AccountDTO.class);
    }

    /**
     * 新增账户和用户信息关联关系
     *
     * @param accountUserConnectionDTOList 账户用户关联关系数据接受对象 List 集合, 单个账户用户关联关系信息为 {@link AccountUserConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void addAccountUserConnection(List<AccountUserConnectionDTO> accountUserConnectionDTOList) {
        accountUserConnectionService.addAccountUserConnection(accountUserConnectionDTOList);
    }

    /**
     * 为账户分配角色信息
     *
     * @param accountRoleConnectionDTOList 账户角色信息接受对象 List 集合, 单个账户角色信息为 {@link AccountRoleConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void allocationAccountRoleConnection(List<AccountRoleConnectionDTO> accountRoleConnectionDTOList) {
        // 1. 判断当前用户是否拥有账户角色添加和删除权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_ACCOUNT_ROLE_CONNECTION.getCode())) {
            log.error("当前用户没有账户角色添加权限!");
            throw new UserException("no.create.permission");
        }
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_ACCOUNT_ROLE_CONNECTION.getCode())) {
            log.error("当前用户没有账户角色删除权限!");
            throw new UserException("no.delete.permission");
        }
        // 2. 判断是否拥有操作数据
        if (CollUtil.isEmpty(accountRoleConnectionDTOList) || accountRoleConnectionDTOList.isEmpty()) {
            log.error("没有需要给账户分配的角色数据!");
            throw new ServiceException("no.data.need.create");
        }
        // 3. 根据账户 ID 删除当前与该账户关联的角色关联信息
        accountRoleConnectionService.removeAccountRoleConnectionByAccountId(AccountRoleConnectionDTO.builder().accountIds(accountRoleConnectionDTOList.stream().map(AccountRoleConnectionDTO::getAccountId).distinct().toList()).build());
        // 4. 新增账户角色关联关系
        accountRoleConnectionService.addAccountRoleConnection(accountRoleConnectionDTOList);
    }

    /**
     * 通过账户 ID 查询账户关联的用户 ID
     *
     * @param accountDTO 账户 ID 信息数据封装对象, 属性值封装, 参见 {@link AccountDTO#getId()} 属性
     * @return 返回属性封装在 {@link AccountDTO#getUserInfoId()}
     */
    public AccountDTO queryUserInfoIdByAccountId(AccountDTO accountDTO){
        if (Objects.isNull(accountDTO.getId())){
            log.error("账户 ID 不能为空!");
            throw new ServiceException("data.incomplete");
        }
        AccountUserConnectionDTO accountUserConnectionDTO = accountUserConnectionService.queryUserInfoIdByAccountId(AccountUserConnectionDTO.builder().accountId(accountDTO.getId()).build());
        return AccountDTO.builder().userInfoId(accountUserConnectionDTO.getUserId()).build();
    }
}
