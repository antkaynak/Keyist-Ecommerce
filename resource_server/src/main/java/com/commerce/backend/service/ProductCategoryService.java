package com.commerce.backend.service;

import com.commerce.backend.model.response.category.ProductCategoryResponse;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategoryResponse> findAllByOrderByName();
}
