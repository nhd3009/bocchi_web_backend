package com.bocchi.bocchiweb.util.specification;

import org.springframework.data.jpa.domain.Specification;

import com.bocchi.bocchiweb.entity.User;

public class UserSpecification {

    public static Specification<User> filterByUsername(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isBlank())
                return null;
            return criteriaBuilder.like(root.get("username"), "%" + username + "%");
        };
    }
}
