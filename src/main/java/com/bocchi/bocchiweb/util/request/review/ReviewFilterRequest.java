package com.bocchi.bocchiweb.util.request.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFilterRequest {
    private String productName;
    private String username;
    private Integer rating;
    private int page = 0;
    private int size = 10;
}