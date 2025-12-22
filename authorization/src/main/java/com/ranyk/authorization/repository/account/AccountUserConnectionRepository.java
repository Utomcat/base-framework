package com.ranyk.authorization.repository.account;

import com.ranyk.model.business.account.entity.AccountUserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
}
