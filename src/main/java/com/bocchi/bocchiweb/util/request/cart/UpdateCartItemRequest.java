package com.bocchi.bocchiweb.util.request.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemRequest {
    private Long productId;
    private Integer quantity;
}
