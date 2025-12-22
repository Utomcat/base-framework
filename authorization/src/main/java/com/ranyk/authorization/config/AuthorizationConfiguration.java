package com.ranyk.authorization.config;

import cn.dev33.satoken.config.SaTokenConfig;
import com.ranyk.authorization.config.properties.AuthorizationConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * CLASS_NAME: AuthorizationConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: SA-TOKEN 授权配置类
 * @date: 2025-10-10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AuthorizationConfigurationProperties.class)
@ConditionalOnProperty(name = "sa-token.enable", havingValue = "true", matchIfMissing = false)
public class AuthorizationConfiguration {

    /**
     * 授权配置属性对象
     */
    private final AuthorizationConfigurationProperties authorizationConfigurationProperties;

    /**
     * 构造方法
     *
     * @param authorizationConfigurationProperties 授权配置属性对象
     */
    @Autowired
    public AuthorizationConfiguration(AuthorizationConfigurationProperties authorizationConfigurationProperties) {
        this.authorizationConfigurationProperties = authorizationConfigurationProperties;
    }

    /**
     * 创建 SA-TOKEN 配置对象
     *
     * @return SA-TOKEN 配置对象 {@link SaTokenConfig}
     */
    @Bean
    @Primary
    public SaTokenConfig saTokenConfig() {
        log.info("Authorization is enabled. create Authorization Object");
        return new SaTokenConfig()
                // token 名称（同时也是 cookie 名称）
                .setTokenName(authorizationConfigurationProperties.getTokenName())
                // token 有效期（单位：秒），默认30天，-1代表永不过期
                .setTimeout(authorizationConfigurationProperties.getTimeout())
                // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
                .setActiveTimeout(authorizationConfigurationProperties.getActivityTimeout())
                // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
                .setIsConcurrent(authorizationConfigurationProperties.getIsConcurrent())
                // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
                .setIsShare(authorizationConfigurationProperties.getIsShare())
                // token 风格
                .setTokenStyle(authorizationConfigurationProperties.getTokenStyle())
                // 是否输出操作日志
                .setIsLog(authorizationConfigurationProperties.getIsLog());
    }
}
