package com.ranyk.model.business.permission.dto;

import com.ranyk.model.base.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: PermissionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端或后端业务类和业务类之间相互传递的权限信息实体封装对象
 * @date: 2025-11-15
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PermissionsDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5291004214727358969L;
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
