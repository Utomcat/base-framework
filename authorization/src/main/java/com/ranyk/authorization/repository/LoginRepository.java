package com.ranyk.authorization.repository;

import com.ranyk.model.business.login.entity.LoginAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CLASS_NAME: LoginRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录业务数据库操作类
 * @date: 2025-10-10
 */
@Repository
public interface LoginRepository  extends JpaRepository<LoginAccount, Long> {

    /**
     * 通过用户名和密码查询用户信息
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户信息
     */
    Optional<LoginAccount> findByUserNameAndPassword(String userName, String password);
}
