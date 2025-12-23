package com.ranyk.authorization.repository.permissions;

import com.ranyk.model.business.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CLASS_NAME: PermissionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限信息数据库操作类
 * @date: 2025-12-18
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, CrudRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    /**
     * 通过传入的权限代码列表, 查询是否存在相同权限代码的数据
     *
     * @param permissionCodeList 需要查询的权限代码 List 集合
     * @return 返回查询结果 Boolean 值, true: 存在相同权限代码的数据; false: 不存在相同权限代码的数据;
     */
    Boolean existsByPermissionCodeIn(List<String> permissionCodeList);

    /**
     * 通过传入的权限代码 List 和 权限 ID 列表, 查询是否存在相同权限代码的数据
     *
     * @param permissionCodeList 需要查询的权限代码 List 集合
     * @param permissionIds      需要查询的权限 ID 列表
     * @return 返回查询结果 Boolean 值, true: 存在相同权限代码的数据; false: 不存在相同权限代码的数据;
     */
    Boolean existsByPermissionCodeInAndIdNotIn(List<String> permissionCodeList, List<Long> permissionIds);

    /**
     * 通过传入的权限 ID 列表, 修改权限信息表中对应的权限状态为指定状态
     *
     * @param ids              需要修改的权限 ID 列表
     * @param permissionStatus 需要修改的权限状态值
     * @param updateId         修改人 ID
     * @param updateTime       修改时间
     * @return 修改的权限数量
     */
    @Modifying
    @Query("update Permission p set p.permissionStatus = :permissionStatus, p.updateId = :updateId, p.updateTime = :updateTime where p.id in :ids and p.permissionStatus != -1")
    Integer updatePermissionStatusByIdIn(@Param("ids") List<Long> ids, @Param("permissionStatus") Integer permissionStatus, @Param("updateId") Long updateId, @Param("updateTime") LocalDateTime updateTime);
}
