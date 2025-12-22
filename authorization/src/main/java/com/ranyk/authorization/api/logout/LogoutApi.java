package com.ranyk.authorization.api.logout;

import com.ranyk.authorization.service.logout.LogoutService;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: LogoutApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户注销接口 API 类
 * @date: 2025-10-11
 */
@RestController
@RequestMapping("/logout")
public class LogoutApi {
    /**
     * 注销业务逻辑类对象
     */
    private final LogoutService logoutService;

    /**
     * 构造方法
     *
     * @param logoutService 注销业务逻辑类对象
     */
    @Autowired
    public LogoutApi(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    /**
     * 用户注销接口
     *
     * @return 注销结果
     */
    @PostMapping
    public R<String> logout() {
        return logoutService.logout() ? R.ok("注销成功！") : R.fail("注销失败！");
    }
}
