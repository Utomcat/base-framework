package com.ranyk.authorization.service.account;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ranyk.authorization.repository.account.AccountRoleConnectionRepository;
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
        // 1. 获取当前登录账户 ID
        Long accountId = StpUtil.getLoginIdAsLong();
        // 2. 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 3. 组装对应的账户角色关联关系数据对象
        List<AccountRoleConnection> needSaveAccountRoleConnectionList = accountRoleConnectionDTOList.stream().map(accountRoleConnectionDTO -> {
            AccountRoleConnection accountRoleConnection = BeanUtil.copyProperties(accountRoleConnectionDTO, AccountRoleConnection.class);
            accountRoleConnection.setCreateId(accountId);
            accountRoleConnection.setUpdateId(accountId);
            accountRoleConnection.setCreateTime(now);
            accountRoleConnection.setUpdateTime(now);
            return accountRoleConnection;
        }).toList();
        // 4. 保存账户角色关联关系数据
        List<AccountRoleConnection> accountRoleConnections = accountRoleConnectionRepository.saveAll(needSaveAccountRoleConnectionList);
        // 5. 判断是否保存成功
        if (!Objects.equals(accountRoleConnections.size(), accountRoleConnectionDTOList.size())) {
            log.error("账户和角色关联关系保存失败, 需要保存的账户和角色关联关系数据量为: {} , 实际保存的账户和角色关联关系数据量为: {}", accountRoleConnectionDTOList.size(), accountRoleConnections.size());
            throw new ServiceException("create.data.fail");
        }
        // 6. 输出日志
        log.info("账户和角色关联关系保存成功, 保存的账户和角色关联关系数据量为: {}", accountRoleConnections.size());
    }

    /**
     * 通过账户 ID 删除对应账户和角色关联关系
     *
     * @param accountRoleConnectionDTO 账户角色关联关系对象,当前主要使用的是 {@link AccountRoleConnectionDTO#getAccountIds()} 属性
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeAccountRoleConnectionByRoleId(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        // 1. 判断是否存在需要处理的数据
        if (Objects.isNull(accountRoleConnectionDTO) || Objects.isNull(accountRoleConnectionDTO.getRoleId())) {
            log.error("{}不需要进行数据处理!", Objects.isNull(accountRoleConnectionDTO) ? "账户角色关联关系对象为空, " : "角色 ID 为空, ");
            throw new ServiceException("no.data.need.delete");
        }
        // 2. 执行删除数据操作, 条件为指定的角色 ID
        Long deleteCount = accountRoleConnectionRepository.deleteByRoleIdEquals(accountRoleConnectionDTO.getRoleId());
        log.info("账户和角色关联关系删除成功, 删除的账户和角色关联关系数据量为: {}", deleteCount.intValue());
    }

    /**
     * 通过角色 ID 查询已关联的账户 ID List 集合
     *
     * @param accountRoleConnectionDTO 账户角色关联关系对象,当前主要使用是 {@link AccountRoleConnectionDTO#getRoleIds()} 属性
     * @return 返回已关联的账户 ID List 集合, 对应的存放属性为 {@link AccountRoleConnectionDTO#getAccountIds()}
     */
    public AccountRoleConnectionDTO queryAccountIdByRoleId(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        List<AccountRoleConnection> queryResult = Optional.of(accountRoleConnectionRepository.findAllByRoleIdIn(accountRoleConnectionDTO.getRoleIds())).orElse(Collections.emptyList());
        if (CollUtil.isEmpty(queryResult)){
            return AccountRoleConnectionDTO.builder().build();
        }
        else {
            return AccountRoleConnectionDTO.builder().accountIds(queryResult.stream().map(AccountRoleConnection::getAccountId).toList()).build();
        }
    }
}
