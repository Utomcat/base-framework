package com.ranyk.model.business.role.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: RoleDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端或后端业务类和业务类之间相互传递的角色信息实体封装对象
 * @date: 2025-11-15
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4312481857172481744L;
    /**
     * 角色数据主键 ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色状态: 1: 正常(默认); -1: 删除/停用;
     */
    private byte roleStatus;
    /**
     * 角色数据创建时间
     */
    private LocalDateTime createTime;
    /**
     * 角色数据创建人 ID
     */
    private Long createId;
    /**
     * 角色数据更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 角色数据更新人 ID
     */
    private Long updateId;

}
