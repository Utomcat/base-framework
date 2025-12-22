package com.ranyk.authorization.service.account;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.repository.account.AccountRoleConnectionRepository;
import com.ranyk.model.business.login.dto.AccountRoleConnectionDTO;
import com.ranyk.model.business.login.entity.AccountRoleConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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

}
