package com.ranyk.common.constant;

import lombok.Getter;

/**
 * CLASS_NAME: PermissionsStatusEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限状态枚举类
 * @date: 2025-12-23
 */
@Getter
public enum PermissionsStatusEnum {
    /**
     * 正常 状态
     */
    NORMAL(1, "正常"),
    /**
     * 删除/停用 状态
     */
    DELETE(-1, "删除/停用"),
    ;

    /**
     * 权限数据状态值
     */
    private final Integer code;
    /**
     * 描述信息
     */
    private final String desc;

    /**
     * 构造函数
     *
     * @param code 权限数据状态值
     * @param desc 描述信息
     */
    PermissionsStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
