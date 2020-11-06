package com.commerce.backend.model.response.product;

import com.commerce.backend.model.dto.ProductVariantDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    private String name;
    private String url;
    private List<ProductVariantDTO> productVariants;
}