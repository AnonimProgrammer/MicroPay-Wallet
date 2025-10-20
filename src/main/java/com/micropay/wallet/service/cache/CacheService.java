package com.micropay.wallet.service.cache;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.function.Supplier;

public interface CacheService {

    <T> T getOrPut(String cacheName, String key, TypeReference<T> type, Supplier<T> supplier);

    void evictAll(String cacheName);

    void evict(String cacheName, String key);
}
