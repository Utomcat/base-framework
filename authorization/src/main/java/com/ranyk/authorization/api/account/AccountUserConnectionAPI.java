package com.ranyk.authorization.api.account;

import com.ranyk.authorization.service.account.AccountUserConnectionService;
import com.ranyk.model.business.account.dto.AccountUserConnectionDTO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CLASS_NAME: AccountUserConnectionAPI.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户关联操作 API 接口类,该接口的功能如下:
 * <p>
 *     <ol>
 *         <li>新增关联账户和用户信息
 *          <ol>
 *             <li>当前登录的账户存在可以新增账户和用户信息关联关系的权限, 权限代码为: add:account:user:connection</li>
 *             <li>账户 ID 不能为空</li>
 *             <li>用户 ID 不能为空</li>
 *             <li>账户和用户信息关联关系不能已存在</li>
 *          </ol>
 *         </li>
 *     </ol>
 * </p>
 * @date: 2025-12-23
 */
@RestController
@RequestMapping("/account/user/connection")
public class AccountUserConnectionAPI {
    /**
     * 账户用户关联关系业务逻辑类对象
     */
    private final AccountUserConnectionService accountUserConnectionService;

    /**
     * 构造函数
     *
     * @param accountUserConnectionService 账户用户关联关系业务逻辑类对象
     */
    @Autowired
    public AccountUserConnectionAPI(AccountUserConnectionService accountUserConnectionService) {
        this.accountUserConnectionService = accountUserConnectionService;
    }

    /**
     * 新增账户和用户信息关联关系
     *
     * @param accountUserConnectionDTOList 账户用户关联关系数据接受对象 List 集合, 单个账户用户关联关系信息为 {@link AccountUserConnectionDTO}
     * @return 新增账户和用户信息关联关系结果
     */
    @PostMapping
    public R<String> addAccountUserConnection(@RequestBody List<AccountUserConnectionDTO> accountUserConnectionDTOList) {
        accountUserConnectionService.addAccountUserConnection(accountUserConnectionDTOList);
        return R.ok("新增账户和用户信息关联关系成功!");
    }
}
