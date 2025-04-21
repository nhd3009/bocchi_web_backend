package com.bocchi.bocchiweb.util.request.order;

import com.bocchi.bocchiweb.util.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
