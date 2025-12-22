package com.ranyk.authorization.repository.role;

import com.ranyk.model.business.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * CLASS_NAME: RoleRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色信息数据库操作类
 * @date: 2025-12-18
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, CrudRepository<Role,Long>, JpaSpecificationExecutor<Role> {

}
