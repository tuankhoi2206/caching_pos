package com.example.caching.config;

import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder().region(Region.US_EAST_1).build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient ddb) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();
    }
}
