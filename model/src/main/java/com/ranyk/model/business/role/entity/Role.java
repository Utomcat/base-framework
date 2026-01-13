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
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_info")
public class Role extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = -8730604650735658652L;
    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false, columnDefinition = "varchar(500) COMMENT '角色名称'")
    private String name;
    /**
     * 角色代码
     */
    @Column(name = "role_code", nullable = false, columnDefinition = "varchar(500) COMMENT '角色代码'")
    private String code;
    /**
     * 角色状态: 1: 正常(默认); -1: 删除/停用;
     */
    @Column(name = "role_status", nullable = false, columnDefinition = "TINYINT DEFAULT 1 COMMENT '角色状态: 1: 正常(默认); -1: 删除/停用;'")
    private Integer status;
}
