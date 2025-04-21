package com.bocchi.bocchiweb.util.request.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryFilterRequest {
    private String name;
    private int page = 0;
    private int size = 10;
}
