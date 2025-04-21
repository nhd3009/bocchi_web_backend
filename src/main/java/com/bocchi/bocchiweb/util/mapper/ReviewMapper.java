package com.bocchi.bocchiweb.util.mapper;

import com.bocchi.bocchiweb.dto.review.ReviewDTO;
import com.bocchi.bocchiweb.entity.Review;

public class ReviewMapper {
    public static ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setUserEmail(review.getUser().getEmail());
        dto.setProductId(review.getProduct().getId());
        return dto;
    }
}