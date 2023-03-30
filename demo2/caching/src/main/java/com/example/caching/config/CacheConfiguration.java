package com.example.caching.config;

import com.example.caching.dynamo.DynamoCacheManage;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Collections;

@Configuration(proxyBeanMethods = false)
public class CacheConfiguration {

    @Bean
    public CacheManagerCustomizer<DynamoCacheManage> cacheManagerCustomizer() {
        return (cacheManager) -> cacheManager.setCaches(Collections.EMPTY_SET);
    }
}
