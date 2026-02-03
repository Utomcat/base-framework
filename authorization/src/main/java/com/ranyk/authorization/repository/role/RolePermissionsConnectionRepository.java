package com.ranyk.authorization.repository.role;

import com.ranyk.model.business.role.entity.RolePermissionConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CLASS_NAME: RolePermissionsConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色权限关联信息数据库操作类
 * @date: 2025-12-18
 */
@Repository
public interface RolePermissionsConnectionRepository extends JpaRepository<RolePermissionConnection, Long>, CrudRepository<RolePermissionConnection, Long>, JpaSpecificationExecutor<RolePermissionConnection> {

    /**
     * 通过角色 ID 列表, 获取对应的角色权限关联信息
     *
     * @param roleIds 需要查询角色权限关联信息的角色 ID 列表
     * @return 返回查询到的角色权限关联信息 List 集合, 单个角色权限关联信息为 {@link RolePermissionConnection} 角色权限关联信息对象;
     */
    List<RolePermissionConnection> findAllByRoleIdIn(List<Long> roleIds);

    /**
     * 通过角色 ID 列表, 删除对应的角色权限关联信息
     *
     * @param roleIds 需要删除角色权限关联信息的角色 ID 列表
     * @return 删除角色权限关联信息数量
     */
    Long deleteByPermissionIdEquals(Long permissionId);

    /**
     * 通过权限 ID 列表, 获取对应的角色权限关联信息
     *
     * @param permissionIds 需要查询角色权限关联信息的权限 ID 列表
     * @return 角色权限关联信息 List 集合, 单个角色权限关联信息为 {@link RolePermissionConnection} 角色权限关联信息对象;
     */
    List<RolePermissionConnection> findByPermissionIdIn(List<Long> permissionIds);
}
