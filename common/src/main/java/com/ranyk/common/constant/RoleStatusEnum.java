package com.ranyk.common.constant;

import lombok.Getter;

/**
 * CLASS_NAME: RoleStatusEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色状态枚举类
 * @date: 2025-12-23
 */
@Getter
public enum RoleStatusEnum {
    /**
     * 角色状态 - 正常
     */
    NORMAL(1, "正常"),
    /**
     * 角色状态 - 删除/停用
     */
    DELETED(-1, "删除/停用"),
    /**
     * 角色状态 - 禁用
     */
    DISABLED(-2, "禁用");

    /**
     * 角色状态 值
     */
    private final Integer code;
    /**
     * 角色状态 描述
     */
    private final String desc;

    /**
     * 构造方法
     *
     * @param code 角色状态 值
     * @param desc 角色状态 描述
     */
    RoleStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
