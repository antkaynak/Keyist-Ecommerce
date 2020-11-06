package com.commerce.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long id;

    private String url;

    private String name;

    private Float price;

    private Integer amount;

    private String thumb;

    private Integer stock;

    private ColorDTO color;
}
