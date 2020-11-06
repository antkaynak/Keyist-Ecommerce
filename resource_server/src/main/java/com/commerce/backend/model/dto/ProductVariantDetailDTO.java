package com.commerce.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductVariantDetailDTO {
    private Long id;
    private String width;
    private String height;
    private String composition;
    private Float price;
    private Float cargoPrice;
    private Float taxPercent;
    private String image;
    private String thumb;
    private Integer stock;
    private Integer live;
    private ColorDTO color;
}