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
    private String name;
    /**
     * 权限代码
     */
    private String code;
    /**
     * 权限类型: 1: 菜单; 2: 按钮; 3: 功能; 4: 其他;
     */
    private Integer type;
    /**
     * 权限状态: 1: 正常(默认); -1: 删除/停用;
     */
    private Integer status;
    /**
     * 权限描述
     */
    private String desc;
    /**
     * 权限备注
     */
    private String remark;
}
