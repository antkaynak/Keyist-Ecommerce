package com.commerce.backend.service;

import com.commerce.backend.model.Product;

public interface ProductService {
    Product findById(Long id);
}
