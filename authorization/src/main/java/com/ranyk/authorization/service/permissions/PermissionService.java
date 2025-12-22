package com.ranyk.authorization.service.permissions;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.repository.permissions.PermissionRepository;
import com.ranyk.authorization.service.role.RolePermissionsConnectionService;
import com.ranyk.model.business.permission.dto.PermissionDTO;
import com.ranyk.model.business.permission.entity.Permission;
import com.ranyk.model.business.role.dto.RolePermissionConnectionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * CLASS_NAME: PermissionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限业务逻辑类
 * @date: 2025-12-18
 */
@Slf4j
@Service
public class PermissionService {
    /**
     * 权限信息数据库操作类对象
     */
    private final PermissionRepository permissionRepository;
    /**
     * 角色权限关联信息数据库操作类对象
     */
    private final RolePermissionsConnectionService rolePermissionsConnectionService;

    /**
     * 构造函数
     *
     * @param permissionRepository             权限信息数据库操作类对象
     * @param rolePermissionsConnectionService 角色权限关联信息业务逻辑类对象
     */
    @Autowired
    public PermissionService(PermissionRepository permissionRepository, RolePermissionsConnectionService rolePermissionsConnectionService) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionsConnectionService = rolePermissionsConnectionService;
    }

    /**
     * 通过传入的角色 ID 列表, 获取对应的权限信息 List 集合
     *
     * @param roleIds 传入的需要查询的 角色 ID List 集合
     * @return 返回查询到的权限信息 List 集合, 单个权限信息为 {@link PermissionDTO} 权限信息对象; 当传入的 roleIds 为 null 时,则返回空权限列表; 当未查询到该账户下的权限信息时,则返回空权限列表;
     */
    public List<PermissionDTO> getPermissionListByRoleIds(List<Long> roleIds) {
        // 1. 判断传入的 roleIds 是否为 null
        if (Objects.isNull(roleIds)) {
            log.error("未传入角色 ID List 集合, 不进行权限信息查询逻辑,直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 2. 判断 roleIds 是否没有元素
        if (roleIds.isEmpty()) {
            log.error("传入的 roleIds 为空, 不进行权限信息查询逻辑,直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 3. 通过传入的 roleIds 获取对应的角色权限关联信息 List 集合
        List<RolePermissionConnectionDTO> rolePermissionConnectionDTOList = rolePermissionsConnectionService.queryRolePermissionConnectionByRoleId(roleIds);
        // 4. 判断角色权限关联信息 List 集合是否没有元素
        if (rolePermissionConnectionDTOList.isEmpty()) {
            log.error("未查询到该账户下的权限信息, 直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 5. 通过角色权限关联信息 List 集合获取对应的权限 ID List 集合
        List<Long> permissionIds = rolePermissionConnectionDTOList.stream().map(RolePermissionConnectionDTO::getPermissionId).toList();
        // 6. 判断权限 ID List 集合是否没有元素
        if (permissionIds.isEmpty()) {
            log.error("未查询到该账户下的权限信息, 直接返回空权限列表!");
            return Collections.emptyList();
        }
        // 7. 通过权限 ID 列表获取对应的权限信息 List 集合
        List<Permission> permissionList = Optional.of(permissionRepository.findAllById(permissionIds)).orElse(Collections.emptyList());
        // 8. 获取权限信息 List 集合,并将其转换为 PermissionDTO 列表
        return BeanUtil.copyToList(permissionList, PermissionDTO.class);
    }
}
