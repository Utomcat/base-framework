package com.ranyk.authorization.service.account;

import com.ranyk.authorization.repository.account.AccountUserConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: AccountUserConnectionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户关联关系业务逻辑类
 * @date: 2025-12-23
 */
@Slf4j
@Service
public class AccountUserConnectionService {

    /**
     * 账户用户关联关系数据库操作类对象
     */
    private final AccountUserConnectionRepository accountUserConnectionRepository;

    /**
     * 构造函数
     *
     * @param accountUserConnectionRepository 账户用户关联关系数据库操作类对象
     */
    public AccountUserConnectionService(AccountUserConnectionRepository accountUserConnectionRepository) {
        this.accountUserConnectionRepository = accountUserConnectionRepository;
    }
}
