package com.commerce.backend.config;

import com.github.benmanes.caffeine.cache.Ticker;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@RunWith(JUnitPlatform.class)
@ComponentScan(basePackages = {"com.commerce.backend.constants"})
class CachingConfigurationTest {

    @Autowired
    private CachingConfiguration cachingConfiguration;

    @Autowired
    private Ticker ticker;

    @Test
    void it_should_load_registered_cache_names() {

        // when
        Collection<String> cacheNames = cachingConfiguration.cacheManager(ticker).getCacheNames();

        // then
        assertThat(cacheNames).hasSize(4).containsExactly(
                "product",
                "product_variant",
                "product_category",
                "product_color"
        );
    }
}
