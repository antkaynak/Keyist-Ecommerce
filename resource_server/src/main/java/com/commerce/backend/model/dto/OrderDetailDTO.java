package com.commerce.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class OrderDetailDTO {

    private String url;

    private String name;

    private Float price;

    private Float cargoPrice;

    private String thumb;

    private Integer amount;

    private CategoryDTO category;

    private ColorDTO color;
}
