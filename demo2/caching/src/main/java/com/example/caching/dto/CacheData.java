package com.example.caching.dto;

import lombok.Data;

@Data
public class CacheData<T> {
    private String key;
    private T value;
}
