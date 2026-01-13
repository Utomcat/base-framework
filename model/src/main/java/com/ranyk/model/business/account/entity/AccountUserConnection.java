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
 * CLASS_NAME: AccountUserConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录账户和用户基本信息关联表实体类封装对象
 * @date: 2025-11-15
 */
@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_user_connection")
public class AccountUserConnection extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = -7043672685508938525L;
    /**
     * 账户信息数据 ID
     */
    @Column(name = "account_id", nullable = false, columnDefinition = "BIGINT COMMENT '账户信息数据 ID'")
    private Long accountId;
    /**
     * 用户信息数据 ID
     */
    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT '用户信息数据 ID'")
    private Long userId;
}
