package com.ranyk.authorization.repository.user;

import com.ranyk.model.business.userinfo.entity.UserBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CLASS_NAME: UserBaseRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户基本信息数据库操作接口
 * @date: 2025-12-22
 */
@Repository
public interface UserBaseRepository extends JpaRepository<UserBase, Long>, CrudRepository<UserBase, Long>, JpaSpecificationExecutor<UserBase> {

    /**
     * 批量更新用户状态 - 删除用户操作
     *
     * @param ids       需要更新的用户 id 列表
     * @param status    新的用户状态
     * @return 返回更新的用户数量
     */
    @Modifying
    @Query("UPDATE UserBase u SET u.status = :status WHERE u.id IN :ids")
    int batchUpdateUserStatusByIds(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
