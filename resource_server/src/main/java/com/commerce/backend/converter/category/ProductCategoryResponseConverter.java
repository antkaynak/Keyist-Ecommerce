package com.commerce.backend.converter.category;

import com.commerce.backend.model.dto.CategoryDTO;
import com.commerce.backend.model.entity.ProductCategory;
import com.commerce.backend.model.response.category.ProductCategoryResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
public class ProductCategoryResponseConverter implements Function<ProductCategory, ProductCategoryResponse> {
    @Override
    public ProductCategoryResponse apply(ProductCategory productCategory) {
        ProductCategoryResponse productCategoryResponse = new ProductCategoryResponse();
        productCategoryResponse.setCategory(CategoryDTO.builder().name(productCategory.getName()).build());
        return productCategoryResponse;
    }
}
