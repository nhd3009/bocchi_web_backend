package com.bocchi.bocchiweb.util.request.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
}
