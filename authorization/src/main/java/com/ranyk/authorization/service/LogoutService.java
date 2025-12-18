package com.ranyk.authorization.service;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: LogoutService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 注销业务逻辑类
 * @date: 2025-10-11
 */
@Service
public class LogoutService {

    /**
     * 注销业务逻辑方法
     *
     * @return 注销结果, {@link Boolean#TRUE} 表示注销成功; {@link Boolean#FALSE} 表示注销失败;
     */
    public Boolean logout() {
        StpUtil.logout();
        return Boolean.TRUE;
    }
}
