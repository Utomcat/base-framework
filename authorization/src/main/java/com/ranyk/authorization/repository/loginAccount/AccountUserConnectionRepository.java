package com.ranyk.authorization.repository.loginAccount;

import com.ranyk.model.business.login.entity.AccountUserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface AccountUserConnectionRepository extends JpaRepository<AccountUserConnection, Long> {
}
