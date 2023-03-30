package com.example.caching;

import com.example.caching.dto.User;
import com.example.caching.dynamo.DynamoCache;
import com.example.caching.dynamo.DynamoCacheManage;
import com.example.caching.dynamo.DynamoCacheResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@SpringBootApplication
@Configuration
@EnableCaching
@Slf4j
public class CachingApplication implements CommandLineRunner {

    @Autowired
    private DynamoDbEnhancedClient client;

    public static void main(String[] args) {
        SpringApplication.run(CachingApplication.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        DynamoCacheManage cacheManager = new DynamoCacheManage(client);
        cacheManager.setCacheNames("user");
        return cacheManager;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Start create table.....");
        createTable(client);
        log.info("End create table.....");
    }

    public static void createTable(DynamoDbEnhancedClient enhancedClient) {

        String tableName = "User";
        if (!tableIsExist(tableName)) {
            // Create a DynamoDbTable object
            DynamoDbTable<User> userTable = enhancedClient.table(tableName, TableSchema.fromBean(User.class));
            // Create the table
            userTable.createTable(builder -> builder
                    .provisionedThroughput(b -> b
                            .readCapacityUnits(10L)
                            .writeCapacityUnits(10L)
                            .build())
            );

            System.out.println("Waiting for table creation...");

            try (DynamoDbWaiter waiter = DynamoDbWaiter.create()) { // DynamoDbWaiter is Autocloseable
                ResponseOrException<DescribeTableResponse> response = waiter
                        .waitUntilTableExists(builder -> builder.tableName(tableName).build())
                        .matched();
                DescribeTableResponse tableDescription = response.response().orElseThrow(
                        () -> new RuntimeException("User table was not created."));
                System.out.println(tableDescription.table().tableName() + " was created.");
            }
        }

    }

    private static boolean tableIsExist(String name) {
        try (DynamoDbWaiter waiter = DynamoDbWaiter.create()) { // DynamoDbWaiter is Autocloseable
            ResponseOrException<DescribeTableResponse> response = waiter
                    .waitUntilTableExists(builder -> builder.tableName(name).build())
                    .matched();
            DescribeTableResponse tableDescription = response.response().orElse(null);

            return tableDescription != null;
        }
    }
}
