package com.ranyk.authorization.permissions;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CLASS_NAME: PermissionsInterfaceImpl.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Sa-Token 权限接口实现类
 * @date: 2025-10-11
 */
@Service
public class PermissionsInterfaceImpl implements StpInterface {

    /**
     * 返回指定账号 id 所拥有的权限码集合
     *
     * @param loginId   账号 id
     * @param loginType 账号类型
     * @return 该账号 id 具有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    /**
     * 返回指定账号 id 所拥有的角色标识集合
     *
     * @param loginId   账号 id
     * @param loginType 账号类型
     * @return 该账号 id 具有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return List.of();
    }
}
