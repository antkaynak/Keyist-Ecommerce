package com.commerce.backend.converter.product;

import com.commerce.backend.model.dto.CategoryDTO;
import com.commerce.backend.model.dto.ColorDTO;
import com.commerce.backend.model.dto.ProductVariantDetailDTO;
import com.commerce.backend.model.entity.Product;
import com.commerce.backend.model.response.product.ProductDetailsResponse;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductDetailsResponseConverter implements Function<Product, ProductDetailsResponse> {
    @Override
    public ProductDetailsResponse apply(Product product) {
        ProductDetailsResponse productDetailsResponse = new ProductDetailsResponse();
        productDetailsResponse.setName(product.getName());
        productDetailsResponse.setUrl(product.getUrl());
        productDetailsResponse.setLongDesc(product.getLongDesc());
        productDetailsResponse.setSku(product.getSku());
        productDetailsResponse.setCategory(CategoryDTO.builder().name(product.getProductCategory().getName()).build());
        productDetailsResponse.setProductVariantDetails(
                product.getProductVariantList()
                        .stream()
                        .map(productVariant -> ProductVariantDetailDTO
                                .builder()
                                .id(productVariant.getId())
                                .width(productVariant.getWidth())
                                .height(productVariant.getHeight())
                                .composition(productVariant.getComposition())
                                .price(productVariant.getPrice())
                                .cargoPrice(productVariant.getCargoPrice())
                                .taxPercent(productVariant.getTaxPercent())
                                .image(productVariant.getImage())
                                .thumb(productVariant.getThumb())
                                .stock(productVariant.getStock())
                                .live(productVariant.getLive())
                                .color(ColorDTO
                                        .builder()
                                        .name(productVariant.getColor().getName())
                                        .hex(productVariant.getColor().getHex())
                                        .build())
                                .build()
                        ).collect(Collectors.toList())
        );

        return productDetailsResponse;
    }
}
