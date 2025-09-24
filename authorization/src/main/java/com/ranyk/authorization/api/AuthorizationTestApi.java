package com.ranyk.authorization.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: AuthorizationTestApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 授权模块测试接口 API 类
 * @date: 2025-09-25
 */
@RestController
@RequestMapping("/authorization/test")
public class AuthorizationTestApi {

    /**
     * 测试接口 one
     * @return java.lang.String
     */
    @GetMapping("/one")
    public String one() {
        return "授权模块测试接口 one";
    }

}
