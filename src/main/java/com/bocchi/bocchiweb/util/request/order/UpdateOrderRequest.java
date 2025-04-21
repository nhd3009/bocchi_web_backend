package com.bocchi.bocchiweb.util.request.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderRequest {
    private String recipientName;
    private String recipientPhone;
    private String shippingAddress;
}
