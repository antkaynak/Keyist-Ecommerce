package com.commerce.backend.service;

import com.commerce.backend.model.entity.ProductVariant;
import com.commerce.backend.model.response.product.ProductDetailsResponse;
import com.commerce.backend.model.response.product.ProductResponse;
import com.commerce.backend.model.response.product.ProductVariantResponse;

import java.util.List;

public interface ProductService {
    ProductDetailsResponse findByUrl(String url);

    List<ProductVariantResponse> getAll(Integer page, Integer size, String sort, String category, Float minPrice, Float maxPrice, String color);

    Long getAllCount(String category, Float minPrice, Float maxPrice, String color);

    ProductVariant findProductVariantById(Long id);

    List<ProductResponse> getRelatedProducts(String url);

    List<ProductResponse> getNewlyAddedProducts();

    List<ProductVariantResponse> getMostSelling();

    List<ProductResponse> getInterested();

    List<ProductResponse> searchProductDisplay(String keyword, Integer page, Integer size);
}
