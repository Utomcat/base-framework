package com.ranyk.authorization.service.role;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.repository.role.RolePermissionsConnectionRepository;
import com.ranyk.model.business.role.dto.RolePermissionConnectionDTO;
import com.ranyk.model.business.role.entity.RolePermissionConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * CLASS_NAME: RolePermissionsConnectionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色权限关联关系业务逻辑类
 * @date: 2025-12-23
 */
@Slf4j
@Service
public class RolePermissionsConnectionService {
    /**
     * 角色权限关联关系数据库操作类对象
     */
    private final RolePermissionsConnectionRepository rolePermissionsConnectionRepository;

    /**
     * 构造函数
     *
     * @param rolePermissionsConnectionRepository 角色权限关联关系数据库操作类对象
     */
    @Autowired
    public RolePermissionsConnectionService(RolePermissionsConnectionRepository rolePermissionsConnectionRepository) {
        this.rolePermissionsConnectionRepository = rolePermissionsConnectionRepository;
    }

    /**
     * 通过角色 ID 列表, 获取对应的角色权限关联关系信息
     *
     * @param roleIdList 需要查询角色权限关联关系的角色 ID 列表
     * @return 角色权限关联关系信息 List 集合, 单个角色权限关联关系信息为 {@link RolePermissionConnectionDTO} 角色权限关联关系信息对象; 当传入的 roleIdList 为 null 时,则返回空角色权限关联关系列表; 当未查询到该账户下的角色权限关联关系信息时,则返回空角色权限关联关系列表;
     */
    public List<RolePermissionConnectionDTO> queryRolePermissionConnectionByRoleId(List<Long> roleIdList) {
        // 通过传入的 roleIdList 获取对应的角色权限关联关系信息
        List<RolePermissionConnection> rolePermissionConnections = Optional.of(rolePermissionsConnectionRepository.findAllByRoleIdIn(roleIdList)).orElse(Collections.emptyList());
        // 将角色权限关联关系信息 List 集合转换为 RolePermissionConnectionDTO 列表
        return BeanUtil.copyToList(rolePermissionConnections, RolePermissionConnectionDTO.class);
    }
}
