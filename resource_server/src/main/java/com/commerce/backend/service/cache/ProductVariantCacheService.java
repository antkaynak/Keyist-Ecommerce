package com.commerce.backend.service.cache;


import com.commerce.backend.model.entity.ProductVariant;

import java.util.List;

public interface ProductVariantCacheService {

    ProductVariant findById(Long id);

    List<ProductVariant> findTop8ByOrderBySellCountDesc();
}
