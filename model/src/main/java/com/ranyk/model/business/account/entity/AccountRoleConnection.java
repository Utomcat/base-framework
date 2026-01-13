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
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_role_connection")
public class AccountRoleConnection extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = 7090502732687351936L;
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
}
