package com.ranyk.model.business.account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: AccountRoleConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户角色关联表实体类封装对象
 * @date: 2025-11-15
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_role_connection")
public class AccountRoleConnection implements Serializable {

    @Serial
    private static final long serialVersionUID = 7090502732687351936L;
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '主键 ID'")
    private Long id;
    /**
     * 账户信息数据 ID
     */
    @Column(name = "account_id", nullable = false, columnDefinition = "BIGINT COMMENT '账户信息数据 ID'")
    private Long accountId;
    /**
     * 角色信息数据 ID
     */
    @Column(name = "role_id", nullable = false, columnDefinition = "BIGINT COMMENT '角色信息数据 ID'")
    private Long roleId;
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
