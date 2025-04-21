package com.bocchi.bocchiweb.dto.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;
    private String userEmail;
    private Long productId;
}
