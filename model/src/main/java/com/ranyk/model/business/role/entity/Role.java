package com.ranyk.model.business.role.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: Role.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色信息实体类封装对象
 * @date: 2025-11-15
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_info")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = -8730604650735658652L;
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '主键 ID'")
    private Long id;
    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false, columnDefinition = "varchar(500) COMMENT '角色名称'")
    private String roleName;
    /**
     * 角色代码
     */
    @Column(name = "role_code", nullable = false, columnDefinition = "varchar(500) COMMENT '角色代码'")
    private String roleCode;
    /**
     * 角色状态: 1: 正常(默认); -1: 删除/停用;
     */
    @Column(name = "role_status", nullable = false, columnDefinition = "TINYINT DEFAULT 1 COMMENT '角色状态: 1: 正常(默认); -1: 删除/停用;'")
    private Integer roleStatus;
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
