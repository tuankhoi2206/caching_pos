package com.example.caching.dynamo;

import com.example.caching.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class DynamoCacheManage extends AbstractCacheManager {

    private DynamoDbEnhancedClient client;

    public DynamoCacheManage(DynamoDbEnhancedClient client) {
        this.client = client;
    }
    private Collection<Cache> caches = new HashSet<>();

    public void setCaches(Collection<Cache> caches) {
        this.caches = caches;
    }

    @Override
    protected Collection<Cache> loadCaches() {
        return this.caches;
    }

    public void setCacheNames(String cacheName) {
        DynamoDbTable<User> userTable = client.table("User", TableSchema.fromBean(User.class));
        Cache cache = new DynamoCache<>(cacheName, userTable, true, null);
        caches.add(cache);
    }
}
