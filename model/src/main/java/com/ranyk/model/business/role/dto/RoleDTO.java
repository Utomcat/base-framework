package com.ranyk.model.business.role.dto;

import com.ranyk.model.base.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: RoleDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端或后端业务类和业务类之间相互传递的角色信息实体封装对象
 * @date: 2025-11-15
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4312481857172481744L;
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
    private Integer status;

    // 以下为额外属性
    /**
     * 账户数据 ID List 集合
     */
    private List<Long> accountIds;
}
