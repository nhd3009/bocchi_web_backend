package com.bocchi.bocchiweb.util.specification;

import org.springframework.data.jpa.domain.Specification;

import com.bocchi.bocchiweb.entity.Review;
import com.bocchi.bocchiweb.util.request.review.ReviewFilterRequest;

public class ReviewSpecification {
    public static Specification<Review> filter(ReviewFilterRequest request) {
        return Specification
                .where(productNameContains(request.getProductName()))
                .and(usernameContains(request.getUsername()))
                .and(ratingEquals(request.getRating()));
    }

    private static Specification<Review> productNameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank())
                return null;
            return cb.like(cb.lower(root.get("product").get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Review> usernameContains(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank())
                return null;
            return cb.like(cb.lower(root.get("user").get("username")), "%" + username.toLowerCase() + "%");
        };
    }

    private static Specification<Review> ratingEquals(Integer rating) {
        return (root, query, cb) -> {
            if (rating == null)
                return null;
            return cb.equal(root.get("rating"), rating);
        };
    }
}
