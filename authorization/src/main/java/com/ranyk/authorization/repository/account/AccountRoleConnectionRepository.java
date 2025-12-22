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
}
