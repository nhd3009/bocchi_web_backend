package com.bocchi.bocchiweb.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.bocchi.bocchiweb.util.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long orderId;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String recipientName;
    private String recipientPhone;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private String email;
    private List<OrderDetailDTO> items;
}
