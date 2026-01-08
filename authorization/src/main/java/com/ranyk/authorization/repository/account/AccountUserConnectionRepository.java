package com.ranyk.authorization.repository.account;

import com.ranyk.model.business.account.entity.AccountUserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * CLASS_NAME: AccountUserConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户关联信息数据库操作类
 * @date: 2025-12-18
 */
@Repository
public interface AccountUserConnectionRepository extends JpaRepository<AccountUserConnection, Long>, CrudRepository<AccountUserConnection, Long>, JpaSpecificationExecutor<AccountUserConnection> {

    /**
     * 依据传入的账户 ID 或 用户 ID 查询是否已经存在数据
     *
     * @param accountIdList 需要查询的账户 ID 列表
     * @param userIdList    需要查询的用户 ID 列表
     * @return 存在数据返回 {@link Boolean#TRUE}; 否则返回 {@link Boolean#FALSE};
     */
    Boolean existsByAccountIdInOrUserIdIn(Set<Long> accountIdList, Set<Long> userIdList);

     /**
     * 依据账户 ID 查询账户用户关联关系数据信息
     *
     * @param accountId 账户 ID
     * @return 账户用户关联关系数据映射实体对象 {@link  AccountUserConnection}
     */
    AccountUserConnection findAccountUserConnectionByAccountIdEquals(Long accountId);
}
