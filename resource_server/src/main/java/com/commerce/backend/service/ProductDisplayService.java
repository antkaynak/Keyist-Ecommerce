package com.commerce.backend.service;


import com.commerce.backend.model.ProductCategory;
import com.commerce.backend.model.ProductDisplay;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductDisplayService {
    ProductDisplay findById(Long id);

    List<ProductDisplay> findAll(Pageable pageable);

    List<ProductDisplay> findAllByProductCategory(Pageable pageable, ProductCategory productCategory);

    List<ProductDisplay> findTop8ByOrderByDateCreatedDesc();

    List<ProductDisplay> findTop8ByOrderBySellCountDesc();

    List<ProductDisplay> findTop8ByOrderBySellCountDescCacheRefresh();

    List<ProductDisplay> getRelatedProducts(ProductCategory productCategory, Long id);

    List<ProductDisplay> searchProducts(String keyword, Integer page, Integer size);
}
