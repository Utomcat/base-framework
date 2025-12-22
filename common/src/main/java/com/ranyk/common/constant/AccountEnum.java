package com.ranyk.common.constant;

import lombok.Getter;

/**
 * CLASS_NAME: AccountEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户常量枚举类 - 用于对指定账户进行标识
 * @date: 2025-12-22
 */
@Getter
public enum AccountEnum {

    /**
     * 超级管理员
     */
    SUPER_ADMIN(1L, "超级管理员");

    /**
     * 账户 ID
     */
    private final Long id;
    /**
     * 账户名称
     */
    private final String name;

    /**
     * 构造方法
     *
     * @param id   账户 ID
     * @param name 账户名称
     */
    AccountEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
