package com.ranyk.authorization.api.user;

import com.ranyk.authorization.service.user.UserService;
import com.ranyk.model.business.userinfo.dto.UserBaseDTO;
import com.ranyk.model.business.userinfo.vo.UserBaseVO;
import com.ranyk.model.page.vo.PageVO;
import com.ranyk.model.response.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CLASS_NAME: UserApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息处理接口 API,接口功能如下:
 * <p>
 *     <ol>
 *         <li>新增用户, 录入用户基本信息,进行数据保存 (批量新增)
 *          <ol>
 *           <li>判断当前用户是否存在新增用户的权限 add:user:info</li>
 *           <li>用户姓氏,为空时默认 '-'</li>
 *           <li>用户名字,为空时默认 '-'</li>
 *           <li>用户性别,为空时默认 0</li>
 *           <li>用户昵称,为空时默认 '-'</li>
 *           <li>用户座机/固定电话,为空时默认 '-'</li>
 *           <li>用户手机号码,为空时默认 '-'</li>
 *           <li>用户邮箱,为空时默认 '-'</li>
 *           <li>用户状态,为空时默认 1</li>
 *          </ol>
 *         </li>
 *         <li>删除用户, 根据数据 ID , 进行用户数据删除(批量更新)
 *          <ol>
 *              <li>判断当前用户是否存在删除用户权限 delete:user:info</li>
 *              <li>用户 ID 不能为空</li>
 *          </ol>
 *         </li>
 *         <li>修改用户, 根据数据 ID , 进行用户数据修改(批量更新)
 *          <ol>
 *              <li>判断当前用户是否存在修改用户权限 update:user:info</li>
 *               <li>用户 ID 不能为空</li>
 *          </ol>
 *         </li>
 *         <li>查询用户, 根据用户条件进行查询, 查询条件如下
 *          <ol>
 *              <li>判断当前用户是否存在查看用户权限 query:user:info</li>
 *              <li>用户 ID</li>
 *              <li>用户全名(模糊匹配)</li>
 *              <li>用户姓氏(模糊匹配)</li>
 *              <li>用户名字(模糊匹配)</li>
 *              <li>用户性别</li>
 *              <li>用户昵称(模糊匹配)</li>
 *              <li>用户座机/固定电话(模糊匹配)</li>
 *              <li>用户手机号码(模糊匹配)</li>
 *              <li>用户邮箱(模糊匹配)</li>
 *              <li>用户状态</li>
 *         </li>
 *     </ol>
 * </p>
 * @date: 2025-12-18
 */
@RestController
@RequestMapping("/user")
public class UserApi {
    /**
     * 用户服务类对象
     */
    private final UserService userService;

    /**
     * 构造方法
     *
     * @param userService 用户服务类对象
     */
    public UserApi(UserService userService) {
        this.userService = userService;
    }

    /**
     * 新增用户, 批量新增
     *
     * @param userBaseDTOList 新增用户信息 List 集合,单个数据参见 {@link UserBaseDTO}
     * @return 新增用户结果
     */
    @PostMapping
    public R<String> addUser(@RequestBody List<UserBaseDTO> userBaseDTOList) {
        userService.addUser(userBaseDTOList);
        return R.ok("新增用户成功!");
    }

    /**
     * 删除用户, 批量删除
     *
     * @param userIdList 删除用户 ID List 集合
     * @return 删除用户结果
     */
    @DeleteMapping
    public R<String> deleteUser(@RequestBody List<Long> userIdList) {
        userService.deleteUser(UserBaseDTO.builder().ids(userIdList).build());
        return R.ok("删除用户成功!");
    }

    /**
     * 修改用户, 批量修改
     *
     * @param userBaseDTOList 修改用户信息 List 集合,单个数据参见 {@link UserBaseDTO}
     * @return 修改用户结果
     */
    @PutMapping
    public R<String> updateUser(@RequestBody List<UserBaseDTO> userBaseDTOList) {
        userService.updateUser(userBaseDTOList);
        return R.ok("修改用户成功!");
    }

    /**
     * 查询用户, 批量查询
     *
     * @param userBaseDTO 查询用户信息,参见 {@link UserBaseDTO}
     * @return 查询用户结果
     */
    @GetMapping
    public R<PageVO<List<UserBaseVO>>> queryUser(UserBaseDTO userBaseDTO) {
        return R.ok(userService.queryUser(userBaseDTO));
    }

}
