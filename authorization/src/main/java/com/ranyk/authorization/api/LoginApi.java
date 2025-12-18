package com.ranyk.authorization.api;

import com.ranyk.authorization.service.LoginService;
import com.ranyk.model.business.login.dto.LoginAccountDTO;
import com.ranyk.model.business.login.vo.LoginAccountVO;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: LoginApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户登录接口 API 类
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
     * @param loginAccountDTO 登录参数封装对象 {@link com.ranyk.model.business.login.dto.LoginAccountDTO}
     * @return 登录结果
     */
    @PostMapping
    public R<LoginAccountVO> login(LoginAccountDTO loginAccountDTO) {
        // 调用登录业务逻辑
        LoginAccountDTO loginResult = loginService.login(loginAccountDTO);
        // 构造返回结果对象
        return R.ok(LoginAccountVO.builder().tokenName(loginResult.getTokenName()).tokenValue(loginResult.getTokenValue()).build());
    }
}
