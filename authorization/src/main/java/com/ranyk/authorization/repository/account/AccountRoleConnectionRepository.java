package com.ranyk.authorization.repository.account;

import com.ranyk.model.business.account.entity.AccountRoleConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CLASS_NAME: AccountRoleConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户角色关联信息数据库操作类
 * @date: 2025-12-18
 */
@Repository
public interface AccountRoleConnectionRepository extends JpaRepository<AccountRoleConnection, Long>, CrudRepository<AccountRoleConnection, Long>, JpaSpecificationExecutor<AccountRoleConnection> {

    /**
     * 通过账户 ID 查询该账户下的 账户角色关联信息
     *
     * @param accountId 需要查询的 账户 ID
     * @return 返回该账户下 账户角色关联信息 List 集合,单个实体参见 {@link AccountRoleConnection} 对象
     */
    List<AccountRoleConnection> findAllByAccountIdEquals(Long accountId);

    /**
     * 通过账户 ID 集合和角色 ID 集合查询指定账户是否已经拥有指定的角色信息
     *
     * @param accountIds 需要查询的账户 ID List 集合
     * @param roleIds    需要查询的角色 ID List 集合
     * @return 返回查询结果, true 表示已经拥有; false 表示没有;
     */
    Boolean existsByAccountIdInAndRoleIdIn(List<Long> accountIds, List<Long> roleIds);

    /**
     * 通过账户 ID 集合删除账户角色关联信息
     *
     * @param accountIds 需要删除的账户 ID List 集合
     * @return 返回删除的账户角色关联信息数量
     */
    Long deleteByAccountIdIn(List<Long> accountIds);
}
