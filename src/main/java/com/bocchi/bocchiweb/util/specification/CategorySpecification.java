package com.bocchi.bocchiweb.util.specification;

import org.springframework.data.jpa.domain.Specification;

import com.bocchi.bocchiweb.entity.Category;

public class CategorySpecification {
    public static Specification<Category> filterByName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank())
                return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
