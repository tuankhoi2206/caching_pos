package com.example.caching.dynamo;

import com.example.caching.dto.CacheData;
import com.example.caching.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.core.serializer.support.SerializationDelegate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

@Slf4j
public class DynamoCache<T> extends AbstractValueAdaptingCache {

    private Class<T> type;

    private String name;

    private DynamoDbTable<T> table;

    @Nullable
    private SerializationDelegate serialization;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    public DynamoCache(String name,
                       DynamoDbTable<T> table,
                       boolean allowNullValues,
                       @Nullable SerializationDelegate serialization) {

        super(allowNullValues);
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(table, "Store must not be null");
        this.name = name;
        this.table = table;
        this.serialization = serialization;
    }

    @Override
    protected Object lookup(Object key) {
        T result = null;
        try {
            Key myKey = Key.builder()
                    .partitionValue((String) key)
                    .sortValue("Mary")
                    .build();

            result = table.getItem((GetItemEnhancedRequest.Builder builder) -> builder.key(myKey));

        } catch (DynamoDbException e) {
            log.error("Cannot found result");
            throw e;
        }
        return result;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.table.scan().items().stream();
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return (T) valueLoader;
    }

    @Override
    public void put(Object key, Object value) {
        Map<Object, Object> data = Map.of(key, value);
        CacheData cacheData = new CacheData<>();
        cacheData.setKey(key.toString());
        cacheData.setValue(value);
        table.putItem(r -> r.item((T) value));
    }

    @Override
    public void evict(Object key) {
        table.deleteItem((DeleteItemEnhancedRequest) key);
    }

    @Override
    public void clear() {

    }
}
