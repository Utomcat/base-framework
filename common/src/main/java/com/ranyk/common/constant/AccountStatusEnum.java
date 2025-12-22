package com.ranyk.common.constant;

import lombok.Getter;

/**
 * CLASS_NAME: AccountStatusEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户状态枚举类
 * @date: 2025-12-19
 */
@Getter
public enum AccountStatusEnum {
    /**
     * 账户状态 - 停用/注销 枚举类对象
     */
    DELETED(-2, "停用/注销"),
    /**
     * 账户状态 - 禁用 枚举类对象
     */
    DISABLED(-1, "禁用"),
    /**
     * 账户状态 - 锁定 枚举类对象
     */
    LOCKED(0, "锁定"),
    /**
     * 账户状态 - 启用 枚举类对象
     */
    ENABLED(1, "启用");

    /**
     * 账户状态 code
     */
    private final Integer code;
    /**
     * 账户状态 name
     */
    private final String name;

    /**
     * 构造方法
     *
     * @param code 账户状态 code
     * @param name 账户状态 name
     */
    AccountStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
