package com.ranyk.cache.uitls;

import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.ranyk.model.exception.service.ServiceException;
import org.jspecify.annotations.NonNull;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * CLASS_NAME: CacheUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 缓存工具类
 * @date: 2025-09-26
 */
public class CacheUtils {

    /**
     * Caffeine 缓存对象
     */
    private final Cache<@NonNull String, Object> caffeineCache = SpringUtil.getBean("caffeineCache");
    /**
     * Redis 缓存对象
     */
    private final RedisTemplate<String, Object> redisTemplate = SpringUtil.getBean("redisTemplate");
    /**
     * 缓存类型: 1: 使用本地缓存; 2: 使用 Redis 缓存;
     */
    private static Integer CACHE_TYPE;

    // 静态代码块初始化缓存类型
    {
        if (Objects.isNull(caffeineCache) && Objects.isNull(redisTemplate)) {
            throw new ServiceException("缓存未启用或未创建对应的缓存对象 caffeineCache 和 redisTemplate !");
        }

        if (Objects.nonNull(caffeineCache)) {
            CACHE_TYPE = 1;
        }
        if (Objects.nonNull(redisTemplate)) {
            CACHE_TYPE = 2;
        }
    }

    /**
     * 缓存数据
     *
     * @param key   缓存的键
     * @param value 缓存的值
     * @return 返回是否操作成功的结果 {@link Boolean}
     */
    public Boolean cache(String key, Object value) {
        if (CACHE_TYPE == 2) {
            redisTemplate.opsForValue().set(key, value);
            return Boolean.TRUE;
        }
        caffeineCache.put(key, value);
        return Boolean.TRUE;
    }

    /**
     * 获取缓存数据
     *
     * @param key 缓存的键
     * @return 返回缓存的值 {@link Object}
     */
    public Object getCache(String key) {
        if (CACHE_TYPE == 2) {
            return redisTemplate.opsForValue().get(key);
        }
        return caffeineCache.getIfPresent(key);
    }

    /**
     * 删除缓存数据
     *
     * @param key 缓存的键
     * @return 删除结果 {@link Boolean}
     */
    public Boolean deleteCache(String key) {
        if (CACHE_TYPE == 2) {
            redisTemplate.delete(key);
            return Boolean.TRUE;
        }
        caffeineCache.invalidate(key);
        return Boolean.TRUE;
    }

}
