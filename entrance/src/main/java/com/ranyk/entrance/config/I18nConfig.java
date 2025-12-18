package com.ranyk.entrance.config;

import com.ranyk.entrance.config.properties.I18nConfigProperties;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * CLASS_NAME: I18nConfig.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 语言环境配置类
 * @date: 2025-12-18
 */
@Configuration
public class I18nConfig {

    /**
     * 语言环境配置属性对象
     */
    private final I18nConfigProperties i18nConfigProperties;

    /**
     * 语言环境配置类对象构造方法
     *
     * @param i18nConfigProperties 语言环境配置属性对象
     */
    @Autowired
    public I18nConfig(I18nConfigProperties i18nConfigProperties) {
        this.i18nConfigProperties = i18nConfigProperties;
    }

    /**
     * 创建语言环境解析器
     *
     * @return 语言环境解析器
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        // 核心：设置默认语言为英语（Locale.US 等价于 new Locale("en", "US")）
        localeResolver.setDefaultLocale(i18nConfigProperties.getLocale());
        return localeResolver;
    }

    /**
     * 添加语言切换拦截器（支持通过?lang=zh_CN临时切换为中文）
     *
     * @return 语言切换拦截器
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // 请求参数名，如?lang=zh_CN
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * 注册拦截器（Spring Boot 3需手动注册）
     *
     * @return 拦截器注册器
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                registry.addInterceptor(localeChangeInterceptor());
            }
        };
    }
}
