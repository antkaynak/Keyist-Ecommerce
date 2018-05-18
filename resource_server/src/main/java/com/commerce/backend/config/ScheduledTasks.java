package com.commerce.backend.config;

import com.commerce.backend.service.ProductDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private ProductDisplayService productDisplayService;

    @Scheduled(fixedDelay = 1800000, initialDelay = 30000) //refresh the most selling product cache in every 30 min
    public void IaaSStatusRefresh() {
        productDisplayService.findTop8ByOrderBySellCountDescCacheRefresh();
    }
}