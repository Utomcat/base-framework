package com.ranyk.authorization.config.properties;

import lombok.Data;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * CLASS_NAME: AuthorizationConfigurationProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: SA-TOKEN 授权配置属性类
 * @date: 2025-10-10
 */
@Data
@Component
@ConfigurationProperties(prefix = "sa-token")
public class AuthorizationConfigurationProperties implements InitializingBean {

    /**
     * token 名称（同时也是 cookie 名称）
     */
    private String tokenName;
    /**
     * token 有效期（单位：秒） 默认30天，-1 代表永久有效
     */
    private Integer timeout;
    /**
     * token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
     */
    private Long activityTimeout;
    /**
     * 是否允许同一账号多地同时登录 （为 true 时允许一起登录, 为 false 时新登录挤掉旧登录）
     */
    private Boolean isConcurrent;
    /**
     * 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token, 为 false 时每次登录新建一个 token）
     */
    private Boolean isShare;
    /**
     * token 风格（默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik）, 未配置则使用默认的 uuid
     */
    private String tokenStyle;
    /**
     * 是否输出操作日志
     */
    private Boolean isLog;



    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 如果未配置 tokenName 时, 默认为 satoken
        if (Objects.isNull(tokenName)){
            this.tokenName = "satoken";
        }
        // 如果未配置 timeout 时, 默认为 2592000 秒
        if (Objects.isNull(timeout)){
            this.timeout = 2592000;
        }
        // 如果未配置 activityTimeout 时, 默认为 -1
        if (Objects.isNull(activityTimeout)){
            this.activityTimeout = -1L;
        }
        // 如果未配置 isConcurrent 时, 默认为 true
        if (Objects.isNull(isConcurrent)){
            this.isConcurrent = Boolean.TRUE;
        }
        // 如果未配置 isShare 时, 默认为 false
        if (Objects.isNull(isShare)){
            this.isShare = Boolean.FALSE;
        }
        // 如果未配置 tokenStyle 时, 默认为 uuid
        if (Objects.isNull(tokenStyle)){
            this.tokenStyle = "uuid";
        }
        // 如果未配置 isLog 时, 默认为 true
        if (Objects.isNull(isLog)){
            this.isLog = Boolean.TRUE;
        }
    }
}
