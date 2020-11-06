package com.commerce.backend.service.cache;

import com.commerce.backend.model.entity.Color;

import java.util.List;

public interface ProductColorCacheService {
    List<Color> findAll();
}
