package com.ranyk.model.business.permission.dto;

import com.ranyk.model.base.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
     * 权限名称
     */
    private String name;
    /**
     * 权限代码
     */
    private String code;
    /**
     * 权限描述
     */
    private String desc;
    /**
     * 权限类型: 1: 菜单; 2: 按钮; 3: 功能; 4: 其他;
     */
    private Integer type;
    /**
     * 权限状态: 1: 正常(默认); -1: 删除/停用;
     */
    private Integer status;

    // 以下为额外属性

    /**
     * 登录账户数据 ID
     */
    private Long accountId;
    /**
     * 角色数据 ID List 集合
     */
    private List<Long> roleIds;

}
