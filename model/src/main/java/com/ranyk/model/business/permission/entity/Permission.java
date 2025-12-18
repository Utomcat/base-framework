package com.ranyk.model.business.permission.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: Permission.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限信息实体类封装对象
 * @date: 2025-11-15
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission_info")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 8832722799279624678L;
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '主键 ID'")
    private Long id;
    /**
     * 权限名称
     */
    @Column(name = "permission_name", nullable = false, columnDefinition = "varchar(500) COMMENT '权限名称'")
    private String permissionName;
    /**
     * 权限代码
     */
    @Column(name = "permission_code", nullable = false, columnDefinition = "varchar(500) COMMENT '权限代码'")
    private String permissionCode;
    /**
     * 权限状态: 1: 正常(默认); -1: 删除/停用;
     */
    @Column(name = "permission_status", nullable = false, columnDefinition = "TINYINT DEFAULT 1 COMMENT '权限状态: 1: 正常(默认); -1: 删除/停用;'")
    private byte permissionStatus;
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
