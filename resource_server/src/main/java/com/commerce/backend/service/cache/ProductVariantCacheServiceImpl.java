package com.commerce.backend.service.cache;

import com.commerce.backend.dao.ProductVariantRepository;
import com.commerce.backend.model.entity.ProductVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "product_variant")
public class ProductVariantCacheServiceImpl implements ProductVariantCacheService {

    private final ProductVariantRepository productVariantRepository;

    @Autowired
    public ProductVariantCacheServiceImpl(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    @Cacheable(key = "{#root.methodName,#id}")
    public ProductVariant findById(Long id) {
        return productVariantRepository.findById(id).orElse(null);
    }

    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<ProductVariant> findTop8ByOrderBySellCountDesc() {
        return productVariantRepository.findTop8ByOrderBySellCountDesc();
    }
}
