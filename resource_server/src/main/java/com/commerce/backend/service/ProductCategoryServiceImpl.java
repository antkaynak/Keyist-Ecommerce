package com.commerce.backend.service;

import com.commerce.backend.dao.ProductCategoryRepository;
import com.commerce.backend.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig(cacheNames = "product_category")
public class ProductCategoryServiceImpl implements ProductCategoryService {


    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    @Cacheable(key = "#category")
    public ProductCategory findByName(String category) {
        return productCategoryRepository.findByName(category);
    }

    @Override
    @Cacheable(key = "#root.methodName")
    public List<ProductCategory> findAllByOrderByName() {
        return productCategoryRepository.findAllByOrderByName();
    }
}
