package com.ranyk.common.constant;

import lombok.Getter;

/**
 * CLASS_NAME: UserStatusEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户数据状态枚举类
 * @date: 2025-12-22
 */
@Getter
public enum UserStatusEnum {
    /**
     * 删除
     */
    DELETE(-1, "删除"),
    /**
     * 无效
     */
    INVALID(0, "无效"),
    /**
     * 正常
     */
    NORMAL(1, "正常"),;

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 构造方法
     *
     * @param code 状态码
     * @param desc 状态描述
     */
    UserStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
