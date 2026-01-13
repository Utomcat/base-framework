package com.ranyk.authorization.api.permissions;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.service.permissions.PermissionsService;
import com.ranyk.model.business.permission.dto.PermissionsDTO;
import com.ranyk.model.business.permission.vo.PermissionsVO;
import com.ranyk.model.page.vo.PageVO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CLASS_NAME: PermissionsApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限信息处理接口 API, 该接口功能如下
 * <p>
 *     <ol>
 *         <li>新增权限
 *          <ol>
 *              <li>当前用户是否拥有新增权限的权限 add:permission:info</li>
 *              <li>权限名称不能为空</li>
 *              <li>权限代码不能为空</li>
 *              <li>权限状态默认为 1</li>
 *          </ol>
 *         </li>
 *         <li>删除权限
 *          <ol>
 *               <li>当前用户是否拥有删除权限的权限 delete:permission:info</li>
 *               <li>需要删除权限数据 ID 不能为空</li>
 *               <li>权限数据的状态是正常的</li>
 *          </ol>
 *         </li>
 *         <li>修改权限
 *          <ol>
 *               <li>当前用户是否拥有修改权限的权限 update:permission:info</li>
 *               <li>权限数据 ID 不能为空</li>
 *               <li>权限名称、权限代码、权限状态不能同时为空</li>
 *           </ol>
 *         </li>
 *         <li>查询权限
 *          <ol>
 *               <li>当前用户是否拥有查询权限的权限 query:permission:info</li>
 *               <li>查询条件如下:
 *                  <ol>
 *                      <li>权限名称, 模糊匹配</li>
 *                      <li>权限代码, 模糊匹配</li>
 *                      <li>权限状态, 精确匹配</li>
 *                  </ol>
 *               </li>
 *           </ol>
 *         </li>
 *     </ol>
 * </p>
 * @date: 2025-12-18
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionsApi {

    /**
     * 权限信息业务逻辑类对象
     */
    private final PermissionsService permissionsService;

    /**
     * 构造函数
     *
     * @param permissionsService 权限信息业务逻辑类对象
     */
    @Autowired
    public PermissionsApi(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    /**
     * 新增权限
     *
     * @param permissionsDTOList 新增权限信息数据封装对象 List 集合, 单个权限信息为 {@link PermissionsDTO}
     * @return 新增权限结果
     */
    @PostMapping
    public R<String> addPermission(@RequestBody List<PermissionsDTO> permissionsDTOList) {
        permissionsService.addPermission(permissionsDTOList);
        return R.ok("权限新增成功!");
    }

    /**
     * 删除权限
     *
     * @param permissionIds 需要删除的权限数据 ID List 集合
     * @return 删除权限结果
     */
    @DeleteMapping
    public R<String> deletePermission(@RequestBody List<Long> permissionIds) {
        permissionsService.deletePermission(PermissionsDTO.builder().ids(permissionIds).build());
        return R.ok("权限删除成功!");
    }

    /**
     * 修改权限
     *
     * @param permissionsDTOList 修改权限信息数据封装对象 List 集合, 单个参见 {@link PermissionsDTO}
     * @return 修改权限结果
     */
    @PutMapping
    public R<String> updatePermission(@RequestBody List<PermissionsDTO> permissionsDTOList) {
        permissionsService.updatePermission(permissionsDTOList);
        return R.ok("权限修改成功!");
    }

    /**
     * 查询权限
     *
     * @param permissionsDTO 查询权限信息数据封装对象, 单个参见 {@link PermissionsDTO}
     * @return 查询权限结果 分页查询结果对象 {@link PageVO}, 单个权限数据参见 {@link PermissionsVO}
     */
    @GetMapping
    public R<PageVO<List<PermissionsVO>>> queryPermissions(PermissionsDTO permissionsDTO) {
        return R.ok(permissionsService.queryPermissions(permissionsDTO));
    }

    /**
     * 根据 角色ID 查询该角色下的权限信息
     *
     * @param roleId 需要查询的 角色ID
     * @return 该角色下的权限信息 List 集合, 单个权限信息参见 {@link PermissionsVO}
     */
    @GetMapping("/role/{roleId}")
    public R<List<PermissionsVO>> getPermissionsByRoleId(@PathVariable Long roleId) {
        return R.ok(BeanUtil.copyToList(permissionsService.getPermissionListByRoleIds(PermissionsDTO.builder().roleIds(List.of(roleId)).build()), PermissionsVO.class));
    }

    /**
     * 根据 账号ID 获取该账号下的权限信息
     *
     * @param accountId 需要查询的 账号ID
     * @return 该账号下的权限信息 List 集合, 单个权限信息参见 {@link PermissionsVO}
     */
    @GetMapping("/account/{accountId}")
    public R<List<PermissionsVO>> getPermissionsByAccountId(@PathVariable Long accountId) {
        return R.ok(BeanUtil.copyToList(permissionsService.getPermissionListByAccountIds(PermissionsDTO.builder().accountId(accountId).build(), Boolean.TRUE), PermissionsVO.class));
    }

    /**
     * 查询当前登录用户的权限信息
     *
     * @return 当前登录用户的权限信息 List 集合, 单个权限信息参见 {@link PermissionsVO}
     */
    @GetMapping("/current/user")
    public R<List<PermissionsVO>> getCurrentUserOfPermissions() {
        return R.ok(BeanUtil.copyToList(permissionsService.getCurrentUserOfPermissions(), PermissionsVO.class));
    }


}
