package com.ranyk.model.business.login.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "login_account_info")
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = -3946869251085240157L;
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '主键 ID'")
    private Long id;
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
    private Integer accountStatus;
    /**
     * 数据创建时间
     */
    @Column(name = "create_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间'")
    private LocalDateTime createTime;
    /**
     * 数据创建人 ID
     */
    @Column(name = "create_id", nullable = false, columnDefinition = "BIGINT DEFAULT 1 COMMENT '数据创建人 ID'")
    private Long createId;
    /**
     * 数据更新时间
     */
    @Column(name = "update_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间'")
    private LocalDateTime updateTime;
    /**
     * 数据更新人 ID
     */
    @Column(name = "update_id", nullable = false, columnDefinition = "BIGINT DEFAULT 1 COMMENT '数据更新人 ID'")
    private Long updateId;
}
