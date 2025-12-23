package com.ranyk.authorization.service.account;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.repository.account.AccountUserConnectionRepository;
import com.ranyk.common.constant.AccountPermissionEnum;
import com.ranyk.model.business.account.dto.AccountUserConnectionDTO;
import com.ranyk.model.business.account.entity.AccountUserConnection;
import com.ranyk.model.exception.service.ServiceException;
import com.ranyk.model.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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

    /**
     * 新增账户和用户信息关联关系
     *
     * @param accountUserConnectionDTOList 账户用户关联关系数据接受对象 List 集合, 单个账户用户关联关系信息为 {@link AccountUserConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void addAccountUserConnection(List<AccountUserConnectionDTO> accountUserConnectionDTOList) {
        // 1. 判断当前账户是否存在新增账户用户关联关系权限
        if (!StpUtil.hasPermission(AccountPermissionEnum.ADD_ACCOUNT_USER_CONNECTION.getCode())) {
            throw new UserException("no.create.permission");
        }
        // 2. 判断账户用户关联关系数据不能为空
        if (accountUserConnectionDTOList == null || accountUserConnectionDTOList.isEmpty()) {
            throw new ServiceException("no.data.need.create");
        }
        // 3. 判断 accountUserConnectionDTOList 中是否存在多个 accountId 或 多个 userId
        var accountIdSet = new HashSet<Long>();
        var userIdSet = new HashSet<Long>();
        for (var connection : accountUserConnectionDTOList) {
            if (Objects.isNull(connection.getAccountId()) || Objects.isNull(connection.getUserId())) {
                log.error("{} 数据为空!!!!", Objects.isNull(connection.getAccountId()) ? "账户 ID" : "用户 ID");
                throw new ServiceException("data.incomplete");
            }
            if (!accountIdSet.add(connection.getAccountId())) {
                log.error("账户ID {} 重复!!!", connection.getAccountId());
                throw new ServiceException("duplicate.data.found");
            }
            if (!userIdSet.add(connection.getUserId())) {
                log.error("用户ID {} 重复!!!", connection.getUserId());
                throw new ServiceException("duplicate.data.found");
            }
        }
        // 4. 判断是否存在入参账户和用户已经绑定的情况
        if (accountUserConnectionRepository.existsByAccountIdInOrUserIdIn(accountIdSet, userIdSet)) {
            log.error("存在账户和用户已经绑定!!!");
            throw new ServiceException("duplicate.data.found");
        }
        // 5. 批量保存账户用户关联关系数据
        List<AccountUserConnection> accountUserConnections = accountUserConnectionRepository.saveAll(accountUserConnectionDTOList.stream().map(accountUserConnectionDTO -> BeanUtil.copyProperties(accountUserConnectionDTO, AccountUserConnection.class)).toList());
        if (accountUserConnections.size() != accountUserConnectionDTOList.size()) {
            log.error("账户用户关联关系数据保存失败!!!");
            throw new ServiceException("data.save.failed");
        }
        log.info("账户用户关联关系数据保存成功!!!");
    }
}
