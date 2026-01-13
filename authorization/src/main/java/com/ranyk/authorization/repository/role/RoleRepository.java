package com.ranyk.authorization.repository.role;

import com.ranyk.model.business.role.entity.Role;
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
 * CLASS_NAME: RoleRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色信息数据库操作类
 * @date: 2025-12-18
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, CrudRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 通过角色代码查询是否已存在对应的角色代码数据
     *
     * @param roleCodeList 角色代码列表
     * @return 返回是否已存在对应的角色代码数据, Boolean 值, true: 已存在; false: 不存在;
     */
    Boolean existsByCodeIn(List<String> roleCodeList);

    /**
     * 通过角色 ID 列表更新角色状态
     *
     * @param ids        角色 ID 列表
     * @param status     角色状态
     * @param updateId   更新账户 ID
     * @param updateTime 更新时间
     * @return 返回更新角色状态数量
     */
    @Modifying
    @Query("update Role r set r.status = :status, r.updateId = :updateId, r.updateTime = r.updateTime where r.id in :ids and r.status != -1")
    Integer updateRoleStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status, @Param("updateId") Long updateId, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 通过角色代码列表和角色 ID 列表查询是否已存在对应的角色代码数据
     *
     * @param roleCodeList 角色代码列表
     * @param idList       角色 ID 列表
     * @return 存在返回 true, 不存在返回 false
     */
    Boolean existsByCodeInAndIdNotInAndStatusEquals(List<String> roleCodeList, List<Long> idList, Integer roleStatus);
}
