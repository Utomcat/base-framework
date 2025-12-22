package com.ranyk.authorization.repository.permissions;

import com.ranyk.model.business.permission.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
}
