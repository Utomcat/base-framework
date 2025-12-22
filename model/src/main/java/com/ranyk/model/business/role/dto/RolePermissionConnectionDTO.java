package com.ranyk.model.business.role.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: RolePermissionConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端或后端业务类之间相互传递的角色权限关联关系信息实体封装类对象
 * @date: 2025-12-23
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionConnectionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4433425073735553667L;

    /**
     * 主键 ID
     */
    private Long id;
    /**
     * 角色信息数据 ID
     */
    private Long roleId;
    /**
     * 权限信息数据 ID
     */
    private Long permissionId;
    /**
     * 数据创建时间
     */
    private LocalDateTime createTime;
    /**
     * 数据创建人 ID
     */
    private Long createId;
    /**
     * 数据更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 数据更新人 ID
     */
    private Long updateId;
}
