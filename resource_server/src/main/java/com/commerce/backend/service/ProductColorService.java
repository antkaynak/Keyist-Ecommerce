package com.commerce.backend.service;

import com.commerce.backend.model.response.color.ProductColorResponse;

import java.util.List;

public interface ProductColorService {
    List<ProductColorResponse> findAll();
}
