package com.commerce.backend.service.cache;

import com.commerce.backend.dao.ColorRepository;
import com.commerce.backend.model.entity.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

@CacheConfig(cacheNames = "product_color")
public class ProductColorCacheServiceImpl implements ProductColorCacheService {

    private final ColorRepository colorRepository;

    @Autowired
    public ProductColorCacheServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<Color> findAll() {
        return colorRepository.findAll();
    }
}
