package com.ranyk.authorization.service.role;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.repository.loginAccount.AccountRoleConnectionRepository;
import com.ranyk.authorization.repository.role.RoleRepository;
import com.ranyk.model.business.login.entity.AccountRoleConnection;
import com.ranyk.model.business.role.dto.RoleDTO;
import com.ranyk.model.business.role.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * CLASS_NAME: RoleService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色业务逻辑处理类
 * @date: 2025-12-18
 */
@Slf4j
@Service
public class RoleService {

    /**
     * 账号角色关联信息数据库操作类
     */
    private final AccountRoleConnectionRepository accountRoleConnectionRepository;
    /**
     * 角色信息数据库操作类
     */
    private final RoleRepository roleRepository;

    /**
     * 构造函数
     *
     * @param accountRoleConnectionRepository 账号角色关联信息数据库操作类
     * @param roleRepository                  角色信息数据库操作类
     */
    @Autowired
    public RoleService(AccountRoleConnectionRepository accountRoleConnectionRepository, RoleRepository roleRepository) {
        this.accountRoleConnectionRepository = accountRoleConnectionRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * 获取当前登录账户的角色信息
     *
     * @return 返回当前登录账户的角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 对象
     */
    public List<RoleDTO> getCurrentUsersRoleList() {
        // 1. 获取当前登录账户的账户 ID, 将其转换为 Long 类型
        Long longinId = Long.valueOf(String.valueOf(StpUtil.getLoginId()));
        // 2. 通过账户 ID 获取该账户下拥有的角色信息
        return getRoleListByAccountId(longinId);
    }

    /**
     * 通过账户 ID 获取该账户下拥有的角色信息
     *
     * @param accountId 需要查询角色信息的账户 ID
     * @return 返回该账户下的角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 角色信息对象; 当未传入账户 ID 时,则返回空角色列表; 当未查询到该账户下的角色信息时,则返回空角色列表;
     */
    public List<RoleDTO> getRoleListByAccountId(Long accountId) {
        // 1. 当传入的账户 ID 为 null,则返回空 List 集合, 说明没有角色 列表
        if (Objects.isNull(accountId)) {
            log.error("未传入的账户 ID, 不进行角色信息查询逻辑,直接返回空角色列表!");
            return Collections.emptyList();
        }
        // 2. 通过传入的 账户 ID 获取该 账户ID 下拥有的 角色 ID
        List<AccountRoleConnection> accountRoleConnectionList = Optional.of(accountRoleConnectionRepository.findAllByAccountIdEquals(accountId)).orElse(Collections.emptyList());
        // 3. 从 accountRoleConnectionList 中获取 roleId List 集合
        List<Long> roleIds = accountRoleConnectionList.stream().map(AccountRoleConnection::getRoleId).toList();
        // 4. 当 roleIds 为空,则返回空 List 集合, 说明没有角色 列表
        if (roleIds.isEmpty()) {
            log.error("未获取到该账户下的角色 ID, 直接返回空角色列表!");
            return Collections.emptyList();
        }
        // 5. 通过角色 ID 获取对应的角色信息 List 集合
        List<Role> roleInfoList = Optional.of(roleRepository.findAllById(roleIds)).orElse(Collections.emptyList());
        // 6. 获取角色信息 List 集合,并将其转换为 RoleDTO 列表
        return BeanUtil.copyToList(roleInfoList, RoleDTO.class);
    }
}
