package com.commerce.backend.model.response.product;

import com.commerce.backend.model.dto.ProductVariantDTO;
import lombok.Data;


@Data
public class ProductVariantResponse {
    private Long id;
    private String name;
    private String url;
    private ProductVariantDTO productVariant;
}
