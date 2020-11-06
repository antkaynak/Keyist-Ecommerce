package com.commerce.backend.converter.color;

import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.entity.Color;
import com.commerce.backend.model.response.color.ProductColorResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProductColorResponseConverter implements Function<Color, ProductColorResponse> {
    @Override
    public ProductColorResponse apply(Color color) {
        ProductColorResponse productColorResponse = new ProductColorResponse();
        productColorResponse.setColor(ColorDTO.builder().name(color.getName()).hex(color.getHex()).build());
        return productColorResponse;
    }
}
