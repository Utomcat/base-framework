package com.ranyk.model.business.role.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_permission_connection")
public class RolePermissionConnection implements Serializable {

    @Serial
    private static final long serialVersionUID = 9074439099606617789L;
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '主键 ID'")
    private Long id;
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
