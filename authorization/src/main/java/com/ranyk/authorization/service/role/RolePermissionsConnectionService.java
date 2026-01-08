package com.ranyk.authorization.service.role;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ranyk.authorization.repository.role.RolePermissionsConnectionRepository;
import com.ranyk.model.business.role.dto.RolePermissionConnectionDTO;
import com.ranyk.model.business.role.entity.RolePermissionConnection;
import com.ranyk.model.exception.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    /**
     * 添加角色权限关联关系信息
     *
     * @param needSaveRolePermissionsList 需要保存的角色权限关联关系信息封装对象 List 集合, 单个角色权限关联关系信息为 {@link RolePermissionConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void addRolePermissionConnection(List<RolePermissionConnectionDTO> needSaveRolePermissionsList) {
        // 1. 判断当前是否存在需要操作的数据
        if (CollUtil.isEmpty(needSaveRolePermissionsList) || needSaveRolePermissionsList.isEmpty()) {
            log.error("未传入需要保存的角色权限关联关系信息, 不进行角色权限关联关系保存逻辑!");
            throw new ServiceException("no.data.need.create");
        }
        // 2. 获取当前登录用户的 ID
        Long currentUserId = StpUtil.getLoginIdAsLong();
        // 3. 获取当前系统时间
        LocalDateTime now = LocalDateTime.now();
        // 4. 构建角色权限关联关系数据库对象集合
        List<RolePermissionConnection> needSaveRolePermissionsConnectionList = needSaveRolePermissionsList.stream().map(rolePermissionConnectionDTO -> RolePermissionConnection.builder().roleId(rolePermissionConnectionDTO.getRoleId()).permissionId(rolePermissionConnectionDTO.getPermissionId()).createId(currentUserId).createTime(now).updateId(currentUserId).updateTime(now).build()).toList();
        // 5. 批量保存角色权限关联关系信息
        List<RolePermissionConnection> rolePermissionConnections = rolePermissionsConnectionRepository.saveAllAndFlush(needSaveRolePermissionsConnectionList);
        if (!Objects.equals(rolePermissionConnections.size(), needSaveRolePermissionsConnectionList.size())) {
            log.error("角色权限关联关系保存失败,需要保存的权限数据量为: {} ,实际保存的权限数据量为: {} !", needSaveRolePermissionsConnectionList.size(), rolePermissionConnections.size());
            throw new ServiceException("create.data.fail");
        }
        log.info("角色权限关联关系保存成功, 保存数量为: {} !", rolePermissionConnections.size());
    }

    /**
     * 删除角色权限关联关系信息
     *
     * @param rolePermissionConnectionDTO 需要删除的角色权限关联关系信息封装对象,当前使用的是 {@link RolePermissionConnectionDTO#getRoleIds} 属性
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeRolePermissionConnectionByRoleId(RolePermissionConnectionDTO rolePermissionConnectionDTO) {
        // 1. 判断是否存在对应需要删除的数据
        if (CollUtil.isEmpty(rolePermissionConnectionDTO.getRoleIds()) || rolePermissionConnectionDTO.getRoleIds().isEmpty()) {
            log.error("未传入需要删除的角色权限关联关系信息, 不进行角色权限关联关系删除逻辑!");
            throw new ServiceException("no.data.need.delete");
        }
        // 2. 执行数据删除
        Long deleteCount = rolePermissionsConnectionRepository.deleteByRoleIdIn(rolePermissionConnectionDTO.getRoleIds());
        log.info("角色权限关联关系删除成功, 删除数量为: {} !", deleteCount.intValue());
    }
}
