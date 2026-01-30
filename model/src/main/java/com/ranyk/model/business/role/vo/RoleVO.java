package com.ranyk.model.business.role.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: RoleVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 后端返回前端角色信息数据实体类封装对象
 * @date: 2025-11-15
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1065853124561955795L;
    /**
     * 角色数据主键 ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色代码
     */
    private String code;
    /**
     * 角色状态: 1: 正常(默认); -1: 删除/停用;
     */
    private byte status;
    /**
     * 角色备注
     */
    private String remark;
}
