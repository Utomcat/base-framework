package com.ranyk.common.constant;

import lombok.Getter;

/**
 * CLASS_NAME: AccountPermissionEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户权限枚举类
 * @date: 2025-12-19
 */
@Getter
public enum AccountPermissionEnum {
    /**
     * 新增账户权限 枚举对象
     */
    ADD_ACCOUNT("add:login:account", "新增账户权限"),
    /**
     * 删除账户权限 枚举对象
     */
    DELETE_ACCOUNT("delete:login:account", "删除账户权限"),
    /**
     * 修改账户权限 枚举对象
     */
    UPDATE_ACCOUNT("update:login:account", "修改账户权限"),
    /**
     * 查询账户权限 枚举对象
     */
    QUERY_ACCOUNT("query:login:account", "查询账户权限"),
    /**
     * 新增用户权限 枚举对象
     */
    ADD_USER_INFO("add:user:info", "新增用户权限"),
    /**
     * 删除用户权限 枚举对象
     */
    DELETE_USER_INFO("delete:user:info", "删除用户权限"),
    /**
     * 修改用户权限 枚举对象
     */
    UPDATE_USER_INFO("update:user:info", "修改用户权限"),
    /**
     * 查询用户权限 枚举对象
     */
    QUERY_USER_INFO("query:user:info", "查询用户权限"),
    ;

    /**
     * 账户权限编码
     */
    private final String code;
    /**
     * 账户权限名称
     */
    private final String name;

    /**
     * 构造函数
     *
     * @param code 账户权限编码
     * @param name 账户权限名称
     */
    AccountPermissionEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
