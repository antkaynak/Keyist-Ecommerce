package com.commerce.backend.service.cache;

import com.commerce.backend.dao.ProductCategoryRepository;
import com.commerce.backend.model.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig(cacheNames = "product_category")
public class ProductCategoryCacheServiceImpl implements ProductCategoryCacheService {

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryCacheServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<ProductCategory> findAllByOrderByName() {
        return productCategoryRepository.findAllByOrderByName();
    }
}
