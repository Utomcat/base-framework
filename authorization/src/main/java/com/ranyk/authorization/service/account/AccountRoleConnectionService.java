package com.ranyk.authorization.service.account;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.repository.account.AccountRoleConnectionRepository;
import com.ranyk.common.constant.AccountPermissionEnum;
import com.ranyk.model.base.dto.BaseDTO;
import com.ranyk.model.business.account.dto.AccountRoleConnectionDTO;
import com.ranyk.model.business.account.entity.AccountRoleConnection;
import com.ranyk.model.exception.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * CLASS_NAME: AccountRoleConnectionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户角色关联关系业务逻辑类
 * @date: 2025-12-23
 */
@Slf4j
@Service
public class AccountRoleConnectionService {

    /**
     * 账户角色关联关系数据库操作类对象
     */
    private final AccountRoleConnectionRepository accountRoleConnectionRepository;

    /**
     * 构造函数
     *
     * @param accountRoleConnectionRepository 账户角色关联关系数据库操作类对象
     */
    @Autowired
    public AccountRoleConnectionService(AccountRoleConnectionRepository accountRoleConnectionRepository) {
        this.accountRoleConnectionRepository = accountRoleConnectionRepository;
    }

    /**
     * 根据账户 ID 查询账户角色关联关系
     *
     * @param accountId 需要查询的账户 ID
     * @return 返回账户角色关联关系对象 List 集合,单个参见 {@link AccountRoleConnectionDTO}
     */
    public List<AccountRoleConnectionDTO> queryAccountRoleConnectionByAccountId(Long accountId) {
        // 通过账户 ID 查询账户角色关联关系, 如果未查询到对应的关联关系, 则返回空 List 集合
        List<AccountRoleConnection> accountRoleConnectionList = Optional.of(accountRoleConnectionRepository.findAllByAccountIdEquals(accountId)).orElse(Collections.emptyList());
        // 将 accountRoleConnectionList 转换为 AccountRoleConnectionDTO 列表并返回
        return BeanUtil.copyToList(accountRoleConnectionList, AccountRoleConnectionDTO.class);
    }

    /**
     * 保存账户角色关联关系
     *
     * @param accountRoleConnectionDTOList 账户角色关联关系对象 List 集合, 单个账户角色关联关系对象参见 {@link AccountRoleConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void addAccountRoleConnection(List<AccountRoleConnectionDTO> accountRoleConnectionDTOList) {
        // 1. 判断当前账户是否拥有账户角色关联关系新增权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_ACCOUNT_ROLE_CONNECTION.getCode())) {
            log.error("当前账户没有账户角色关联关系新增权限");
            throw new ServiceException("no.create.permission");
        }
        // 2. 判断赋权的账户是否已经拥有指定的角色
        List<Long> accountIds = accountRoleConnectionDTOList.stream().map(AccountRoleConnectionDTO::getAccountId).toList();
        List<Long> roleIds = accountRoleConnectionDTOList.stream().map(AccountRoleConnectionDTO::getRoleId).toList();
        if (accountRoleConnectionRepository.existsByAccountIdInAndRoleIdIn(accountIds, roleIds)) {
            log.error("账户已经拥有指定的角色");
            throw new ServiceException("duplicate.data.found");
        }
        // 3. 获取当前登录账户 ID
        Long accountId = StpUtil.getLoginIdAsLong();
        // 4. 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 5. 组装对应的账户角色关联关系数据对象
        List<AccountRoleConnection> needSaveAccountRoleConnectionList = accountRoleConnectionDTOList.stream().map(accountRoleConnectionDTO -> {
            AccountRoleConnection accountRoleConnection = BeanUtil.copyProperties(accountRoleConnectionDTO, AccountRoleConnection.class);
            accountRoleConnection.setCreateId(accountId);
            accountRoleConnection.setUpdateId(accountId);
            accountRoleConnection.setCreateTime(now);
            accountRoleConnection.setUpdateTime(now);
            return accountRoleConnection;
        }).toList();
        // 6. 保存账户角色关联关系数据
        List<AccountRoleConnection> accountRoleConnections = accountRoleConnectionRepository.saveAll(needSaveAccountRoleConnectionList);
        // 7. 判断是否保存成功
        if (!Objects.equals(accountRoleConnections.size(), accountRoleConnectionDTOList.size())) {
            log.error("账户和角色关联关系保存失败, 需要保存的账户和角色关联关系数据量为: {} , 实际保存的账户和角色关联关系数据量为: {}", accountRoleConnectionDTOList.size(), accountRoleConnections.size());
            throw new ServiceException("create.data.fail");
        }
        // 8. 输出日志
        log.info("账户和角色关联关系保存成功, 保存的账户和角色关联关系数据量为: {}", accountRoleConnections.size());
    }

    /**
     * 删除账户角色关联关系
     *
     * @param accountRoleConnectionDTO 账户角色关联关系对象,参见 {@link AccountRoleConnectionDTO}, 该处使用的是 {@link AccountRoleConnectionDTO} 的父类 {@link BaseDTO} 的额外公共属性 {@link BaseDTO#getIds()}
     */
    @Transactional
    public void deleteAccountRoleConnection(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        // 1. 判断当前用户是否存在删除账户和角色关联关系权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.DELETE_ACCOUNT_ROLE_CONNECTION.getCode())) {
            log.error("当前用户没有删除账户和角色关联关系权限");
            throw new ServiceException("no.delete.permission");
        }
        // 2. 判断是否存在需要处理的数据
        if (Objects.isNull(accountRoleConnectionDTO.getIds()) || accountRoleConnectionDTO.getIds().isEmpty()){
            log.error("不存在需要处理的数据");
            throw new ServiceException("no.data.need.delete");
        }
        // 3. 执行删除操作
        accountRoleConnectionRepository.deleteAllById(accountRoleConnectionDTO.getIds());
    }
}
