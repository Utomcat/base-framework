package com.ranyk.model.business.account.entity;

import com.ranyk.model.base.entity.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginAccount.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录账户信息实体类封装对象
 * @date: 2025-10-10
 */
@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "login_account_info")
public class Account extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = -3946869251085240157L;
    /**
     * 用户名
     */
    @Column(name = "login_user_name", nullable = false, columnDefinition = "varchar(500) COMMENT '账户登录用户名'")
    private String userName;
    /**
     * 密码
     */
    @Column(name = "login_password", nullable = false, columnDefinition = "varchar(500) COMMENT '账户登录密码'")
    private String password;
    /**
     * 登录账户状态: -2: 停用/注销; -1: 禁用; 0: 锁定; 1: 启用;
     */
    @Column(name = "login_account_status", nullable = false, columnDefinition = "TINYINT DEFAULT 0 COMMENT '登录账户状态: -2: 停用/注销; -1: 禁用; 0: 锁定; 1: 启用;'")
    private Integer status;
}
