package com.ranyk.authorization.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.ranyk.model.exception.user.UserException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CLASS_NAME: SaTokenInterptorcsConfig.java
 *
 * @author ranyk
 * @version V1.0
 * @description: sa-token 拦截器配置类
 * @date: 2025-12-18
 */
@Configuration
public class SaTokenInterceptorsConfig implements WebMvcConfigurer {

    /**
     * 注册Sa-Token拦截器，配置路由鉴权规则
     *
     * @param registry 拦截器注册对象 {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器并配置鉴权逻辑
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 1. 所有未放行的接口，必须先登录
                    SaRouter.match("/**")
                            // 未登录会抛出 NotLoginException
                            .check(r -> {
                                try {
                                    StpUtil.checkLogin();
                                } catch (NotLoginException e) {
                                    throw new UserException("user.not.login");
                                }
                            });
                }))
                // 拦截所有请求路径
                .addPathPatterns("/**")
                // 忽略所有无需鉴权的接口 登录接口、静态资源接口、图标接口
                .excludePathPatterns("/login", "/static/**", "/favicon.ico");
    }
}
