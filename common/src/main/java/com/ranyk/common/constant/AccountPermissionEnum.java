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
    /**
     * 新增用户角色权限 枚举对象
     */
    ADD_ROLE_INFO("add:role:info", "新增用户角色权限"),
    /**
     * 删除用户角色权限 枚举对象
     */
    DELETE_ROLE_INFO("delete:role:info", "删除用户角色权限"),
    /**
     * 修改用户角色权限 枚举对象
     */
    UPDATE_ROLE_INFO("update:role:info", "修改用户角色权限"),
    /**
     * 查询用户角色权限 枚举对象
     */
    QUERY_ROLE_INFO("query:role:info", "查询用户角色权限"),
    /**
     * 新增账户用户关联关系权限 枚举对象
     */
    ADD_ACCOUNT_USER_CONNECTION("add:account:user:connection", "新增账户用户关联关系权限"),
    /**
     * 新增权限信息权限 枚举对象
     */
    ADD_PERMISSIONS_INFO("add:permissions:info", "新增权限信息权限"),
    /**
     * 删除权限信息权限 枚举对象
     */
    DELETE_PERMISSIONS_INFO("delete:permissions:info", "删除权限信息权限"),
    /**
     * 修改权限信息权限 枚举对象
     */
    UPDATE_PERMISSIONS_INFO("update:permissions:info", "修改权限信息权限"),
    /**
     * 查询权限信息权限 枚举对象
     */
    QUERY_PERMISSIONS_INFO("query:permissions:info", "查询权限信息权限"),
    /**
     * 新增账户角色关联关系权限 枚举对象
     */
    ADD_ACCOUNT_ROLE_CONNECTION("add:account:role:connection", "新增账户角色关联关系权限"),
    /**
     * 删除账户角色关联关系权限 枚举对象
     */
    DELETE_ACCOUNT_ROLE_CONNECTION("delete:account:role:connection", "删除账户角色关联关系权限"),
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
