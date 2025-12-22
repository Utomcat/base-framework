package com.ranyk.authorization.repository.loginAccount;

import com.ranyk.model.business.login.entity.LoginAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CLASS_NAME: LoginAccountRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户信息数据库操作类
 * @date: 2025-10-10
 */
@Repository
public interface LoginAccountRepository extends JpaRepository<LoginAccount, Long>, CrudRepository<LoginAccount, Long>, JpaSpecificationExecutor<LoginAccount> {

    /**
     * 通过账户名和密码查询有效的账户信息
     *
     * @param userName      用户名
     * @param password      密码
     * @param accountStatus 账户状态
     * @return 用户信息
     */
    Optional<LoginAccount> findByUserNameAndPasswordAndAccountStatusEquals(String userName, String password, Integer accountStatus);

    /**
     * 查询是否存在一条指定账户名的数据
     *
     * @param userName 需要查询的账户名
     * @return 返回是否存在数据, true: 存在; false: 不存在;
     */
    boolean existsByUserName(String userName);

    /**
     * 查询是否存在非 指定ID 的 指定账户名 数据
     *
     * @param userName 需要匹配的账户名
     * @param id       不需要匹配的账户 id
     * @return 返回是否存在数据, true: 存在; false: 不存在;
     */
    boolean existsByUserNameEqualsAndIdNot(String userName, Long id);

    /**
     * 批量注销账户
     *
     * @param ids       需要注销的账户 id 列表
     * @param newStatus 新的账户状态
     * @return 返回注销的账户数量
     */
    @Modifying
    @Query("UPDATE LoginAccount la SET la.accountStatus = :newStatus WHERE la.id IN :ids AND la.accountStatus != -2")
    int batchDeregistrationAccountStatusByIds(@Param("ids") List<Long> ids, @Param("newStatus") Integer newStatus);
}
