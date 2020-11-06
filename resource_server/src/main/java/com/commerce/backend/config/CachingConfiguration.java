package com.commerce.backend.config;


import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
@EnableCaching
public class CachingConfiguration {
    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        CaffeineCache productCache = buildCache("product", ticker, 10, MINUTES);
        CaffeineCache productDisplayCache = buildCache("product_variant", ticker, 10, MINUTES);
        CaffeineCache productCategoryCache = buildCache("product_category", ticker, 1, HOURS);
        CaffeineCache productColorCache = buildCache("product_color", ticker, 1, HOURS);

        SimpleCacheManager manager = new SimpleCacheManager();

        manager.setCaches(Arrays.asList(productCache, productDisplayCache, productCategoryCache, productColorCache));

        return manager;
    }

    private CaffeineCache buildCache(String name, Ticker ticker, long duration, TimeUnit unit) {
        return new CaffeineCache(name, newBuilder()
                .expireAfterWrite(duration, unit)
                .maximumSize(500_000)
                .executor(Executors.newCachedThreadPool())
                .ticker(ticker)
                .build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
}