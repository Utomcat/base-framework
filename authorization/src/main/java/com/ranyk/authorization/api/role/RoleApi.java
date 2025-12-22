package com.ranyk.authorization.api.role;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.service.role.RoleService;
import com.ranyk.model.business.role.vo.RoleVO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/role")
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
    @GetMapping
    public R<List<RoleVO>> queryCurrentUsersRoleList() {
        return R.ok(BeanUtil.copyToList(roleService.getCurrentUsersRoleList(), RoleVO.class));
    }
}
