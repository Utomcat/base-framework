package com.ranyk.model.business.permission.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: PermissionVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 后端返回前端权限信息实体类封装对象
 * @date: 2025-11-15
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PermissionsVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 9094887326472742619L;
    /**
     * 权限数据主键 ID
     */
    private Long id;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限代码
     */
    private String permissionCode;
    /**
     * 权限状态: 1: 正常(默认); -1: 删除/停用;
     */
    private Integer permissionStatus;
}
