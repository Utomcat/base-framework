package com.ranyk.authorization.api.account;

import cn.hutool.core.bean.BeanUtil;
import com.ranyk.authorization.service.account.AccountService;
import com.ranyk.model.business.account.dto.AccountDTO;
import com.ranyk.model.business.account.dto.AccountUserConnectionDTO;
import com.ranyk.model.business.account.vo.AccountVO;
import com.ranyk.model.page.vo.PageVO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * CLASS_NAME: AccountApi.java
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
@RequestMapping("/api/account")
public class AccountApi {

    /**
     * 登录账户业务逻辑类对象
     */
    private final AccountService accountService;

    /**
     * 构造方法
     *
     * @param accountService 登录账户业务逻辑类对象
     */
    @Autowired
    public AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 新增账户信息
     *
     * @param accountDTO 登录账户信息封装对象, {@link AccountDTO}
     * @return 返回新增的账户信息对象 {@link AccountVO}
     */
    @PostMapping
    public R<AccountVO> addLoginAccount(@RequestBody AccountDTO accountDTO) {
        return R.ok(BeanUtil.copyProperties(accountService.addLoginAccount(accountDTO), AccountVO.class));
    }

    /**
     * 登录账户注销
     *
     * @param accountDTOList 需要注销的登录账户信息封装对象 List 集合, 单个参见 {@link AccountDTO}
     * @return 返回注销账户信息结果
     */
    @DeleteMapping
    public R<String> revokedLoginAccount(@RequestBody List<AccountDTO> accountDTOList) {
        accountService.revokedLoginAccount(accountDTOList);
        return R.ok("注销账户成功!");
    }

    /**
     * 修改账户信息
     *
     * @param accountDTOList 修改的账户信息封装对象 List 集合, 单个参见 {@link AccountDTO}
     * @return 修改账户信息结果
     */
    @PutMapping
    public R<String> updateLoginAccount(@RequestBody List<AccountDTO> accountDTOList) {
        accountService.updateLoginAccount(accountDTOList);
        return R.ok("修改账户成功!");
    }

    /**
     * 查询账户信息
     *
     * @param accountDTO 查询条件封装对象, 单个参见 {@link AccountDTO}, 主要是 登录用户名、账户状态两种查询条件
     * @return 查询账户信息结果 List 集合,单个参见 {@link AccountVO}
     */
    @GetMapping
    public R<PageVO<List<AccountVO>>> queryLoginAccount(AccountDTO accountDTO) {
        return R.ok(accountService.queryLoginAccount(accountDTO));
    }

    /**
     * 通过用户 ID 查询对应用户绑定的账户信息
     *
     * @param accountDTO 查询条件封装对象, 此处传入的参数为 {@link AccountDTO#getUserInfoId()} 属性
     * @return 返回该用户绑定的账号信息封装对象 {@link AccountVO}
     */
    @GetMapping("/query/account/by/userInfoId")
    public R<AccountVO> queryAccountInfoByUserInfoId(AccountDTO accountDTO) {
        return R.ok(BeanUtil.copyProperties(accountService.queryAccountInfoByUserInfoId(accountDTO), AccountVO.class));
    }

    /**
     * 查询未绑定用户信息的账户信息 List 集合
     *
     * @return 查询账户信息结果 List 集合, 单个参见 {@link AccountVO}
     */
    @GetMapping("/not/bound")
    public R<PageVO<List<AccountVO>>> queryNotBoundAccount() {
        return R.ok(accountService.queryNotBoundAccount());
    }

    /**
     * 新增账户和用户信息关联关系
     *
     * @param accountUserConnectionDTOList 账户用户关联关系数据接受对象 List 集合, 单个账户用户关联关系信息为 {@link AccountUserConnectionDTO}
     * @return 新增账户和用户信息关联关系结果
     */
    @PostMapping("/user/connection")
    public R<String> addAccountUserConnection(@RequestBody List<AccountUserConnectionDTO> accountUserConnectionDTOList) {
        accountService.addAccountUserConnection(accountUserConnectionDTOList);
        return R.ok("新增账户和用户信息关联关系成功!");
    }

    /**
     * 查询所有有效的账户信息 List 集合
     *
     * @param accountDTO 查询条件封装对象, 此处传入的参数为 {@link AccountDTO#getStatus()} 属性
     * @return 返回查询到的有效账户信息 List 集合, 单个参见 {@link AccountVO}
     */
    @GetMapping("/query/all/effective/account")
    public R<List<AccountVO>> queryAllEffectiveAccount(AccountDTO accountDTO) {
        return R.ok(BeanUtil.copyToList(accountService.queryAllEffectiveAccount(accountDTO), AccountVO.class));
    }

    /**
     * 查询账户已绑定的角色信息的账户 ID List 集合
     *
     * @param accountDTO 查询条件封装对象, 此处传入的参数为 {@link AccountDTO#getRoleId()} 属性
     * @return 返回查询到的已绑定指定角色的账户信息 ID List 集合, 此处返回 String 类型是为了防止前端浏览器对 Long 类型的数据丢失处理
     */
    @GetMapping("/query/connection/role/of/account/id")
    public R<List<String>> queryConnectionRoleOfAccountId(AccountDTO accountDTO) {
        AccountDTO queryResult = accountService.queryConnectionRoleOfAccountId(accountDTO);
        return R.ok(queryResult.getIds().isEmpty() ? Collections.emptyList() : queryResult.getIds().stream().map(String::valueOf).toList());
    }
}
