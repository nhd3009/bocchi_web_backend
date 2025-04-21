package com.bocchi.bocchiweb.dto.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Double averageRating;
    private String imgUrl;
    private Long categoryId;
    private String categoryName;
}
