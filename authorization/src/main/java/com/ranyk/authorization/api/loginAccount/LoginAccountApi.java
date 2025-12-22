package com.ranyk.authorization.api.loginAccount;

import com.ranyk.authorization.service.loginAccount.LoginAccountService;
import com.ranyk.model.business.login.dto.LoginAccountDTO;
import com.ranyk.model.business.login.vo.LoginAccountVO;
import com.ranyk.model.page.vo.PageVO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CLASS_NAME: LoginAccountApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录账户接口 API, 接口功能如下:
 * <p>
 *     <ol>
 *         <li>登录账户信息添加:
 *          <ol>
 *              <li>添加账户信息,添加条件如下:
 *               <ol>
 *                   <li>当前登录的账户存在可以新增账户的权限, 权限代码为: add:login:account</li>
 *                   <li>登录账户名不能为空</li>
 *                   <li>登录密码不能为空</li>
 *                   <li>登录账户默认为启用状态: 1</li>
 *                   <li>登录账户的账户名不能已存在</li>
 *               </ol>
 *              </li>
 *          </ol>
 *         </li>
 *         <li>登录账户信息删除(逻辑删除,修改账户的状态为 -2):
 *          <ol>
 *              <li>当前登录的账户存在可以删除账户的权限, 权限代码为: delete:login:account</li>
 *              <li>登录账户户 id 不能为空</li>
 *              <li>登录账户状态不能为 -2</li>
 *          </ol>
 *         </li>
 *         <li>登录账户信息修改:
 *          <ol>
 *              <li>当前登录的账户存在可以修改账户的权限, 权限代码为: update:login:account</li>
 *              <li>可修改内容如下:
 *               <ol>
 *                   <li>登录账户名
 *                    <ol>
 *                        <li>登录账户名不能为空</li>
 *                        <li>登录账户名不能已存在</li>
 *                    </ol>
 *                   </li>
 *                   <li>登录密码
 *                    <ol>
 *                         <li>登录密码不能为空</li>
 *                    </ol>
 *                   </li>
 *                   <li>登录账户状态: -1: 禁用; 0: 锁定; 1: 启用;</li>
 *               </ol>
 *              </li>
 *          </ol>
 *         </li>
 *         <li>登录账户信息查询:
 *          <ol>
 *              <li>当前登录的账户存在可以查询账户的权限, 权限代码为: query:login:account</li>
 *              <li>通过账户 ID 查询(精准匹配)</li>
 *              <li>通过账户名查询(模糊查询)</li>
 *              <li>返回结果是查询到的账户信息 List 集合, 如果未查询到则返回一个没有元素的 List 空集合,禁止返回一个 null</li>
 *          </ol>
 *         </li>
 *     </ol>
 * </p>
 * @date: 2025-12-18
 */
@RestController
@RequestMapping("/account")
public class LoginAccountApi {

    /**
     * 登录账户业务逻辑类对象
     */
    private final LoginAccountService loginAccountService;

    /**
     * 构造方法
     *
     * @param loginAccountService 登录账户业务逻辑类对象
     */
    @Autowired
    public LoginAccountApi(LoginAccountService loginAccountService) {
        this.loginAccountService = loginAccountService;
    }

    /**
     * 新增账户信息
     *
     * @param loginAccountDTO 登录账户信息封装对象, {@link LoginAccountDTO}
     * @return 新增账户信息结果
     */
    @PostMapping
    public R<String> addLoginAccount(@RequestBody LoginAccountDTO loginAccountDTO) {
        loginAccountService.addLoginAccount(loginAccountDTO);
        return R.ok("新增账户成功!");
    }

    /**
     * 登录账户注销
     *
     * @param loginAccountDTOList 需要注销的登录账户信息封装对象 List 集合, 单个参见 {@link LoginAccountDTO}
     * @return 返回注销账户信息结果
     */
    @DeleteMapping
    public R<String> revokedLoginAccount(@RequestBody List<LoginAccountDTO> loginAccountDTOList) {
        loginAccountService.revokedLoginAccount(loginAccountDTOList);
        return R.ok("注销账户成功!");
    }

    /**
     * 修改账户信息
     *
     * @param loginAccountDTOList 修改的账户信息封装对象 List 集合, 单个参见 {@link LoginAccountDTO}
     * @return 修改账户信息结果
     */
    @PutMapping
    public R<String> updateLoginAccount(@RequestBody List<LoginAccountDTO> loginAccountDTOList) {
        loginAccountService.updateLoginAccount(loginAccountDTOList);
        return R.ok("修改账户成功!");
    }

    /**
     * 查询账户信息
     *
     * @param loginAccountDTO 查询条件封装对象, 单个参见 {@link LoginAccountDTO}, 主要是 登录用户名、账户状态两种查询条件
     * @return 查询账户信息结果 List 集合,单个参见 {@link LoginAccountVO}
     */
    @GetMapping
    public R<PageVO<List<LoginAccountVO>>> queryLoginAccount(LoginAccountDTO loginAccountDTO) {
        return R.ok(loginAccountService.queryLoginAccount(loginAccountDTO));
    }


}
