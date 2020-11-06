package com.commerce.backend.service.cache;

import com.commerce.backend.model.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryCacheService {
    List<ProductCategory> findAllByOrderByName();
}
