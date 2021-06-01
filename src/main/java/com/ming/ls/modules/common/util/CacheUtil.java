package com.ming.ls.modules.common.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * @PackgeName: com.gdcccn.oa.modules.common.utils
 * @Author: nza
 * Date: 2019/10/15 10:44
 */
@Slf4j
public class CacheUtil {
    private static LoadingCache<String, Object> localCache = CacheBuilder
                        .newBuilder()
                        .initialCapacity(1000)
                        .maximumSize(100000)   // 超过 100000 使用lRU(最小使用算法)算法自动清除
                        .expireAfterAccess(30, TimeUnit.DAYS)
                        .build(new CacheLoader<String, Object>() {
                            // 默认的加载实现,当调用get取值时, 如果没有key对应的值, 就调用这个方法进行加载
                            @Override
                            public Object load(String s) throws Exception {
                                return Lists.newArrayList();
                            }
                        });


    /**
     * 设置缓存
     * @param key       键
     * @param value     值
     */
    public static void setKey(String key, Object value) {
        localCache.put(key, value);
    }

    /**
     * 获取缓存值
     * @param key 键
     * @return Object
     */
    public static Object getKey(String key) {
        Object res;
        try {
            res = localCache.get(key);
            return res;
        } catch (Exception e) {
            log.error("localCache get error", e);
            return null;
        }
    }

    /**
     * 删除缓存
     * @param key 键
     */
    public static void del(String key) {
        localCache.invalidate(key);
    }
}
