package com.ranyk.cache.config.properties;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * CLASS_NAME: CacheConfigurationProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 缓存配置属性对象
 * @date: 2025-09-26
 */
@Data
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheConfigurationProperties implements InitializingBean {
    /**
     * 是否启用 Caffeine 本地缓存
     */
    private Boolean caffeineEnabled;
    /**
     * Caffeine 本地缓存初始化大小
     */
    private Integer caffeineInitSize;
    /**
     * Caffeine 本地缓存最大缓存条数
     */
    private Integer caffeineMaxSize;
    /**
     * 是否输出缓存移除日志
     */
    private Boolean isOutPutLogWarn;
    /**
     * 是否启用 Redis 缓存
     */
    private Boolean redisEnabled;
    /**
     * 使用 Redis 作为缓存时的 redis 缓存数据库主机地址
     */
    private String redisHost;
    /**
     * 使用 Redis 作为缓存时的 redis 缓存数据库端口号
     */
    private Integer redisPort;
    /**
     * 使用 Redis 作为缓存时的 redis 缓存数据库密码
     */
    private String redisPassword;
    /**
     * 使用 Redis 作为缓存时的 redis 缓存数据库索引
     */
    private Integer redisDatabase;


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
        // 当未进行 caffeine 缓存配置时,默认开启 caffeine 缓存
        if (Objects.isNull(caffeineEnabled)){
            this.caffeineEnabled = Boolean.TRUE;
        }

        // 当未进行 redis 缓存配置时,默认关闭 redis 缓存
        if (Objects.isNull(redisEnabled)){
            this.redisEnabled = Boolean.FALSE;
        }

        // 当未进行 redis 和 caffeine 缓存配置时,默认使用 caffeine 本地缓存
        if (!caffeineEnabled && !redisEnabled){
            // 启用 caffeine 本地缓存
            this.caffeineEnabled =  Boolean.TRUE;
            // 关闭 redis 缓存
            this.redisEnabled = Boolean.FALSE;
        }

        // 当配置为使用 Caffeine 进行缓存时,进行参数判断
        if (caffeineEnabled){
            // 未配置 Caffeine 初始化缓存大小时,使用默认值 100
            if (Objects.isNull(caffeineInitSize)){
                this.caffeineInitSize = 100;
            }
            // 未配置 Caffeine 缓存最大缓存条数时,使用默认值 500
            if (Objects.isNull(caffeineMaxSize)){
                this.caffeineMaxSize = 500;
            }
            // 未配置是否输出缓存移除日志时,使用默认值 false
            if (Objects.isNull(isOutPutLogWarn)){
                this.isOutPutLogWarn = Boolean.FALSE;
            }
        }

        // 当配置为使用 Redis 进行缓存时,进行参数判断
        if (redisEnabled){
            // 未配置 Redis 的主机地址时,使用默认值 "localhost"
            if (StrUtil.isBlank(redisHost)){
                this.redisHost = "localhost";
            }
            // 未配置 Redis 的端口号时,使用默认值 6379
            if (Objects.isNull(redisPort)){
                this.redisPort = 6379;
            }
            // 未配置 Redis 的密码时,使用默认值 ""
            if (StrUtil.isBlank(redisPassword)){
                this.redisPassword = "";
            }
            // 未配置 Redis 的数据库索引时,使用默认值 0
            if (Objects.isNull(redisDatabase)){
                this.redisDatabase = 0;
            }
        }
    }
}
