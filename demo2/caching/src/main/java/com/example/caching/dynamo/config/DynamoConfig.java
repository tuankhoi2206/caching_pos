package com.example.caching.dynamo.config;

import com.example.caching.dynamo.DynamoCacheManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@Configuration
@Slf4j
public class DynamoConfig {

//    @Autowired
//    private DynamoDbEnhancedClient client;
//
//    @Bean
//    public CacheManager cacheManager() {
//        log.info("Creating cache with name: user");
//        DynamoCacheManage cacheManager = new DynamoCacheManage(client);
//        cacheManager.setCacheNames("user");
//        return cacheManager;
//    }

}
