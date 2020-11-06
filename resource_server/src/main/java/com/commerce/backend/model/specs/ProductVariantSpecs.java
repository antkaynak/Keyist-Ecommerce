package com.commerce.backend.model.specs;

import com.commerce.backend.model.entity.ProductVariant;
import org.springframework.data.jpa.domain.Specification;


public final class ProductVariantSpecs {

    public static Specification<ProductVariant> withCategory(String category) {
        return (root, query, cb) -> {
            if (category == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.join("product").join("productCategory").get("name"), category);
        };
    }

    public static Specification<ProductVariant> withColor(String color) {
        return (root, query, cb) -> {
            if (color == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.equal(root.get("color").get("name"), color);
        };
    }

    public static Specification<ProductVariant> maxPrice(Float price) {
        return (root, query, cb) -> {
            if (price == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.lessThan(root.get("price"), price);
        };
    }

    public static Specification<ProductVariant> minPrice(Float price) {
        return (root, query, cb) -> {
            if (price == null) {
                return cb.isTrue(cb.literal(true));
            }
            return cb.greaterThanOrEqualTo(root.get("price"), price);
        };
    }

}
