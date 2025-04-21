package com.bocchi.bocchiweb.util.request.order;

import java.time.LocalDate;

import com.bocchi.bocchiweb.util.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFilterRequest {
    private OrderStatus status;
    private String recipientName;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;
    private int page = 0;
    private int size = 10;
}
