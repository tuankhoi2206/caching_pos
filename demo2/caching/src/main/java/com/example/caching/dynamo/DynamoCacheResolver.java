package com.example.caching.dynamo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The SimpleCacheResolver class is the default resolver used by spring in the cache annotation.
 * We add additional operations by extending the SimpleCacheResolver class.
 */
@Slf4j
public class DynamoCacheResolver extends SimpleCacheResolver {
    public DynamoCacheResolver(CacheManager cacheManager) {
        super(cacheManager);
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<String> cacheNames = getCacheNames(context);
        if (cacheNames == null) {
            log.info("ResolveCaches is null");
            return Collections.emptyList();
        }
        Collection<Cache> caches = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            Cache cache = getCacheManager().getCache(cacheName);
//            if (cache == null) {
//                throw new IllegalArgumentException("Cannot find cache named '" +
//                        cacheName + "' for " + context.getOperation());
//            }
            log.info("ResolveCaches with cache name: {}", cacheName);
            caches.add(cache);
        }
        return caches;
    }
}
