package com.commerce.backend.config;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class EhCacheConfig implements CachingConfigurer {

    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration productDisplayCacheConfiguration = new CacheConfiguration();
        productDisplayCacheConfiguration.setName("product_display");
        productDisplayCacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        productDisplayCacheConfiguration.setMaxEntriesLocalHeap(1000);

        CacheConfiguration productCacheConfiguration = new CacheConfiguration();
        productCacheConfiguration.setName("product");
        productCacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        productCacheConfiguration.setMaxEntriesLocalHeap(1000);

        CacheConfiguration productCategoryCacheConfiguration = new CacheConfiguration();
        productCategoryCacheConfiguration.setName("product_category");
        productCategoryCacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        productCategoryCacheConfiguration.setMaxEntriesLocalHeap(1000);

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(productDisplayCacheConfiguration);
        config.addCache(productCacheConfiguration);
        config.addCache(productCategoryCacheConfiguration);

        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        ehCacheCacheManager.setCacheManager(ehCacheManager());
        ehCacheCacheManager.setTransactionAware(true);
        return ehCacheCacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }
}