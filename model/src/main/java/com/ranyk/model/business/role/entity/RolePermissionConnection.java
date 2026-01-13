package com.ranyk.model.business.role.entity;

import com.ranyk.model.base.entity.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: RolePermissionConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色权限关联表实体类封装对象
 * @date: 2025-11-15
 */
@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_permission_connection")
public class RolePermissionConnection extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = 9074439099606617789L;
    /**
     * 角色信息数据 ID
     */
    @Column(name = "role_id", nullable = false, columnDefinition = "BIGINT COMMENT '角色信息数据 ID'")
    private Long roleId;
    /**
     * 权限信息数据 ID
     */
    @Column(name = "permission_id", nullable = false, columnDefinition = "BIGINT COMMENT '权限信息数据 ID'")
    private Long permissionId;
}
