package com.commerce.backend.service;

import com.commerce.backend.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    ProductCategory findByName(String category);

    List<ProductCategory> findAllByOrderByName();
}
