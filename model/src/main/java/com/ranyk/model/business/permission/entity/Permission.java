package com.ranyk.model.business.permission.entity;

import com.ranyk.model.base.entity.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

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
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permission_info")
public class Permission extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = 8832722799279624678L;

    /**
     * 权限名称
     */
    @Column(name = "permission_name", nullable = false, columnDefinition = "varchar(500) COMMENT '权限名称'")
    private String name;
    /**
     * 权限代码
     */
    @Column(name = "permission_code", nullable = false, columnDefinition = "varchar(500) COMMENT '权限代码'")
    private String code;
    /**
     * 权限描述
     */
    @Column(name = "permission_desc", columnDefinition = "varchar(500) COMMENT '权限描述'")
    private String desc;
    /**
     * 权限类型: 1: 菜单; 2: 按钮; 3: 功能; 4: 其他;
     */
    @Column(name = "permission_type", nullable = false, columnDefinition = "TINYINT DEFAULT 1 COMMENT '权限类型: 1: 菜单; 2: 按钮; 3: 功能; 4: 其他;'")
    private Integer type;
    /**
     * 权限状态: 1: 正常(默认); -1: 删除/停用;
     */
    @Column(name = "permission_status", nullable = false, columnDefinition = "TINYINT DEFAULT 1 COMMENT '权限状态: 1: 正常(默认); -1: 删除/停用;'")
    private Integer status;
}
