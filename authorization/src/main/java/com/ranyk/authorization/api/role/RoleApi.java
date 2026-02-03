package com.ranyk.authorization.api.role;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ranyk.authorization.service.role.RoleService;
import com.ranyk.model.business.role.dto.RoleDTO;
import com.ranyk.model.business.role.vo.RoleVO;
import com.ranyk.model.page.vo.PageVO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * CLASS_NAME: RoleApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色信息处理接口 API
 * @date: 2025-12-18
 */
@RestController
@RequestMapping("/api/role")
public class RoleApi {

    /**
     * 角色信息处理类对象
     */
    private final RoleService roleService;

    /**
     * 构造方法
     *
     * @param roleService 角色信息处理类对象
     */
    @Autowired
    public RoleApi(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 查询当前登录账户的角色信息
     *
     * @return 返回当前已登录的账户角色信息 List 集合, 单个角色信息为 {@link RoleVO} 对象
     */
    @GetMapping("current/user")
    public R<List<RoleVO>> queryCurrentUsersRoleList() {
        return R.ok(BeanUtil.copyToList(roleService.getCurrentUsersRoleList(), RoleVO.class));
    }

    /**
     * 新增角色信息
     *
     * @param roleDTOList 新增角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 角色信息对象
     * @return 新增角色信息结果
     */
    @PostMapping
    public R<String> addRole(@RequestBody List<RoleDTO> roleDTOList){
        roleService.addRole(roleDTOList);
        return R.ok("新增角色成功!");
    }

    /**
     * 删除角色信息
     *
     * @param roleIdList 删除角色信息 List 集合, 单个角色信息为 Long 角色 ID
     * @return 删除角色信息结果
     */
    @DeleteMapping
    public R<String> deleteRole(@RequestBody List<Long> roleIdList){
        roleService.deleteRole(RoleDTO.builder().ids(roleIdList).build());
        return R.ok("删除角色成功!");
    }

    /**
     * 更新角色信息
     *
     * @param roleDTOList 更新角色信息 List 集合, 单个角色信息为 {@link RoleDTO} 角色信息对象
     * @return 更新角色信息结果
     */
    @PutMapping
    public R<String> updateRole(@RequestBody List<RoleDTO> roleDTOList){
        roleService.updateRole(roleDTOList);
        return R.ok("更新角色成功!");
    }

    /**
     * 查询角色信息
     *
     * @param roleDTO 查询角色信息参数, 单个角色信息为 {@link RoleDTO} 角色信息对象
     * @return 查询角色信息结果
     */
    @GetMapping
    public R<PageVO<List<RoleVO>>> queryRoleList(RoleDTO roleDTO){
        return R.ok(roleService.queryRoleList(roleDTO));
    }

    /**
     * 为角色分配账户
     *
     * @param roleDTO 角色和账户关联的参数封装对象, 其中主要使用封装的 {@link RoleDTO#getId()} 和 {@link RoleDTO#getAccountIds()} 属性
     * @return 为角色分配账户结果
     */
    @PostMapping("/assigned/account")
    public R<String> assignedAccountForRole(@RequestBody RoleDTO roleDTO) {
        roleService.assignedAccountForRole(roleDTO);
        return R.ok("为账户分配角色成功!");
    }

    /**
     * 查询所有有效角色
     *
     * @param roleDTO 查询条件封装对象, 此处传入的参数为 {@link RoleDTO#getStatus()} 状态属性
     * @return 返回查询到的角色信息 List 集合, 单个参见 {@link RoleVO}
     */
    @GetMapping("/query/all/effective/role")
    public R<List<RoleVO>> queryAllEffectiveRole(RoleDTO roleDTO){
        return R.ok(BeanUtil.copyToList(roleService.queryAllEffectiveRole(roleDTO), RoleVO.class));
    }

    /**
     * 查询角色已关联的权限信息的角色 ID List 集合
     *
     * @param roleDTO 查询条件封装对象, 此处传入的参数为 {@link RoleDTO#getId()} 角色 ID
     * @return 返回查询到的已关联权限的角色 ID List 集合
     */
    @GetMapping("/query/connection/permission/of/role/id")
    public R<List<String>> queryConnectionPermissionOfRoleId(RoleDTO roleDTO){
        RoleDTO queryResult = roleService.queryConnectionPermissionOfRoleId(roleDTO);
        return R.ok(CollUtil.isEmpty(queryResult.getIds()) ? Collections.emptyList() : queryResult.getIds().stream().map(String::valueOf).toList());
    }
}
