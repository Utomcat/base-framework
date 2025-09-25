package com.ranyk.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ranyk.cache.config.properties.CacheConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * CLASS_NAME: CacheConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 缓存配置类对象
 * @date: 2025-09-26
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CacheConfigurationProperties.class)
@ConditionalOnProperty(name = "cache.enable", havingValue = "true", matchIfMissing = false)
public class CacheConfiguration {

    /**
     * 缓存配置属性对象
     */
    private final CacheConfigurationProperties cacheConfigurationProperties;

    /**
     * 缓存配置类对象构造方法
     *
     * @param cacheConfigurationProperties 缓存配置属性对象
     */
    @Autowired
    public CacheConfiguration(CacheConfigurationProperties cacheConfigurationProperties) {
        this.cacheConfigurationProperties = cacheConfigurationProperties;
    }

    /**
     * 常规缓存,未设置指定的过期时间,如果需要设置过期时间,则需要另外配置一个缓存
     *
     * @return 返回构建好的缓存对象 {@link Cache}
     */
    @Bean("caffeineCache")
    @ConditionalOnProperty(name = "cache.is-caffeine-enabled", havingValue = "true", matchIfMissing = false)
    public Cache<@NonNull String, Object> caffeineCache() {
        log.info("CaffeineCache is enabled. create Caffeine Cache Object");
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(cacheConfigurationProperties.getCaffeineInitSize())
                // 缓存的最大条数
                .maximumSize(cacheConfigurationProperties.getCaffeineMaxSize())
                // 缓存移除监听器
                .removalListener((key, value, cause) -> {if (cacheConfigurationProperties.getIsOutPutLogWarn()){log.warn("caffeineCache Cache failure listening => key: {}, value: {}, cause: {}", key, value, cause);}})
                // 开启统计功能
                .recordStats()
                .build();
    }

    /**
     * 配置 Redis 连接工厂
     *
     * @return 返回 Redis 连接工厂对象 {@link RedisConnectionFactory}
     */
    @Bean
    @ConditionalOnProperty(name = "cache.is-redis-enabled", havingValue = "true", matchIfMissing = false)
    public LettuceConnectionFactory redisFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(cacheConfigurationProperties.getRedisHost());
        redisConfiguration.setPort(cacheConfigurationProperties.getRedisPort());
        redisConfiguration.setPassword(cacheConfigurationProperties.getRedisPassword());
        redisConfiguration.setDatabase(cacheConfigurationProperties.getRedisDatabase());
        return new LettuceConnectionFactory(redisConfiguration);
    }

    /**
     * 配置 Redis 缓存模板
     *
     * @param redisFactory Redis 连接工厂对象,上面创建的连接工厂
     * @return Redis 缓存模板对象 {@link RedisTemplate}
     */
    @Bean
    @ConditionalOnProperty(name = "cache.is-redis-enabled", havingValue = "true", matchIfMissing = false)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisFactory) {
        log.info("RedisCache is enabled. create Redis Cache Object");
        // 创建 RedisTemplate对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(redisFactory);
        // 创建字符串序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        // 创建 JSON 序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置 key 的序列化
        template.setKeySerializer(stringSerializer);
        // 设置 value 的序列化
        template.setValueSerializer(jsonSerializer);
        // 设置 hash key 的序列化
        template.setHashKeySerializer(stringSerializer);
        // 设置 hash value 的序列化
        template.setHashValueSerializer(jsonSerializer);
        // 初始化参数设置
        template.afterPropertiesSet();

        return template;
    }
}
