package com.commerce.backend.service.cache;

import com.commerce.backend.model.entity.Product;
import com.commerce.backend.model.entity.ProductCategory;

import java.util.List;

public interface ProductCacheService {

    Product findByUrl(String url);

    List<Product> findTop8ByOrderByDateCreatedDesc();

    List<Product> getRelatedProducts(ProductCategory productCategory, Long id);

}
