package com.ranyk.authorization.service.permissions;

import cn.dev33.satoken.stp.StpInterface;
import com.ranyk.authorization.service.role.RoleService;
import com.ranyk.model.business.permission.dto.PermissionDTO;
import com.ranyk.model.business.role.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
     * 角色信息业务逻辑类对象
     */
    private final RoleService roleService;
    /**
     * 权限信息业务逻辑类对象
     */
    private final PermissionService permissionService;

    /**
     * 构造函数
     *
     * @param roleService       角色信息业务逻辑类对象
     * @param permissionService 权限信息业务逻辑类对象
     */
    @Autowired
    public PermissionsInterfaceImpl(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    /**
     * 返回指定账号 id 所拥有的权限码集合
     *
     * @param loginId   账号 id
     * @param loginType 账号类型
     * @return 该账号 id 具有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 1. 获取当前登录账户的账户 ID, 将其转换为 Long 类型
        Long longLoginId = Long.valueOf(String.valueOf(loginId));
        // 2. 通过账户 ID 获取该账户下拥有的角色信息
        List<RoleDTO> roleDTOList = roleService.getRoleListByAccountId(longLoginId);
        // 3.  从 roleDTOList 中获取 roleId List 集合
        List<Long> roleIds = roleDTOList.stream().map(RoleDTO::getId).toList();
        // 4. 通过角色ID List 集合获取对应账户下的权限信息 List 集合
        List<PermissionDTO> permissionCodes = permissionService.getPermissionListByRoleIds(roleIds);
        // 5. 去掉 permissionCodes 中 permissionCode 重复的对象, 去重逻辑：保留第一个出现的相同permissionCode对象
        return permissionCodes.stream()
                .collect(Collectors.toMap(
                        PermissionDTO::getPermissionCode,
                        Function.identity(),
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .map(PermissionDTO::getPermissionCode)
                .toList();
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
        // 1. 获取当前登录账户的账户 ID, 将其转换为 Long 类型
        Long longLoginId = Long.valueOf(String.valueOf(loginId));
        // 2. 通过账户 ID 获取该账户下拥有的角色信息
        List<RoleDTO> roleDTOList = roleService.getRoleListByAccountId(longLoginId);
        // 3. 去掉 roleDTOList 中 roleCode 重复的对象, 去重逻辑：保留第一个出现的相同roleCode对象
        List<RoleDTO> distinctRoleList = roleDTOList.stream()
                // 以roleCode为键构建Map，重复键时保留第一个对象
                .collect(Collectors.toMap(
                        // 键：提取roleCode
                        RoleDTO::getRoleCode,
                        // 值：当前RoleDTO对象
                        Function.identity(),
                        // 冲突策略：保留前者
                        (existing, replacement) -> existing
                ))
                // 提取Map的值并转换为List（JDK 21中Collection.stream()和toList()保持兼容）
                .values()
                .stream()
                // JDK 16+ 引入的 toList()，返回不可变列表；若需可变列表可改用Collectors.toList()
                .toList();
        // 4. 返回角色标识集合
        return distinctRoleList.stream().map(RoleDTO::getRoleCode).toList();
    }
}
