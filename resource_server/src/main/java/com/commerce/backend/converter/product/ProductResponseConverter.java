package com.commerce.backend.converter.product;

import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.dto.ProductVariantDTO;
import com.commerce.backend.model.entity.Product;
import com.commerce.backend.model.response.product.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class ProductResponseConverter implements Function<Product, ProductResponse> {

    @Override
    public ProductResponse apply(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setUrl(product.getUrl());
        productResponse.setProductVariants(product.getProductVariantList()
                .stream()
                .map(variant -> ProductVariantDTO
                        .builder()
                        .id(variant.getId())
                        .price(variant.getPrice())
                        .thumb(variant.getThumb())
                        .stock(variant.getStock())
                        .color(ColorDTO
                                .builder()
                                .name(variant.getColor().getName())
                                .hex(variant.getColor().getHex())
                                .build())
                        .build())
                .collect(Collectors.toList()));

        return productResponse;
    }
}
