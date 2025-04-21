package com.bocchi.bocchiweb.util.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.bocchi.bocchiweb.entity.Product;
import com.bocchi.bocchiweb.util.request.product.ProductFilterRequest;

public class ProductSpecification {

    public static Specification<Product> filter(ProductFilterRequest request) {
        return Specification.where(nameContains(request.getName()))
                .and(categoryEquals(request.getCategoryId()))
                .and(priceGreaterThanOrEqualTo(request.getMinPrice()))
                .and(priceLessThanOrEqualTo(request.getMaxPrice()));
    }

    private static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty())
                return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Product> categoryEquals(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null)
                return null;
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    private static Specification<Product> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, cb) -> {
            if (minPrice == null)
                return null;
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    private static Specification<Product> priceLessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null)
                return null;
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
}