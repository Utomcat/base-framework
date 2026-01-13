package com.ranyk.model.business.role.dto;

import com.ranyk.model.base.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: RolePermissionConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端或后端业务类之间相互传递的角色权限关联关系信息实体封装类对象
 * @date: 2025-12-23
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RolePermissionConnectionDTO extends BaseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4433425073735553667L;

    /**
     * 角色信息数据 ID
     */
    private Long roleId;
    /**
     * 权限信息数据 ID
     */
    private Long permissionId;

    // 以下为额外属性
    /**
     * 角色 ID List 集合
     */
    private List<Long> roleIds;
    /**
     * 权限 ID List 集合
     */
    private List<Long> permissionIds;
}
