package com.commerce.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductVariantDTO {
    private Long id;
    private Float price;
    private String thumb;
    private Integer stock;
    private ColorDTO color;
}
