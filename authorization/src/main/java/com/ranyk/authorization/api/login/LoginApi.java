package com.ranyk.authorization.api.login;

import com.ranyk.authorization.service.login.LoginService;
import com.ranyk.model.business.account.dto.AccountDTO;
import com.ranyk.model.business.login.vo.LoginAccountInfoVO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: LoginApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户登录接口 API 类, 接口功能如下:
 * <p>
 *     <ol>
 *      <li>账户登录,登录条件:
 *       <ol>
 *           <li>录入的账户名不能为空</li>
 *           <li>录入的密码不能为空</li>
 *           <li>登录账户状态为有效</li>
 *           <li>登录账户名存在</li>
 *       </ol>
 *      </li>
 *      <li>账户登录成功后返回结果
 *       <ol>
 *           <li>登录账户的 SA-TOKEN 认证令牌 token 名称</li>
 *           <li>登录账户的 SA-TOKEN 认证令牌 token 值</li>
 *       </ol>
 *      </li>
 *     </ol>
 * </p>
 * @date: 2025-10-10
 */
@RestController
@RequestMapping("/login")
public class LoginApi {

    /**
     * 登录业务逻辑类对象
     */
    private final LoginService loginService;

    /**
     * 构造方法
     *
     * @param loginService 登录业务逻辑类对象
     */
    @Autowired
    public LoginApi(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 用户登录接口
     *
     * @param accountDTO 登录参数封装对象 {@link AccountDTO}
     * @return 登录结果
     */
    @PostMapping
    public R<LoginAccountInfoVO> login(@RequestBody AccountDTO accountDTO) {
        // 调用登录业务逻辑
        AccountDTO loginResult = loginService.login(accountDTO);
        // 构造返回结果对象
        return R.ok(LoginAccountInfoVO.builder().tokenName(loginResult.getTokenName()).tokenValue(loginResult.getTokenValue()).account(loginResult.getUserName()).userAccount(loginResult.getUserAccount()).avatar(loginResult.getAvatar()).build());
    }
}
