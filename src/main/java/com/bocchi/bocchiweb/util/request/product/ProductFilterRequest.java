package com.bocchi.bocchiweb.util.request.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterRequest {
    private String name;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private int page = 0;
    private int size = 10;
}
