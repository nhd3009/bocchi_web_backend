package com.bocchi.bocchiweb.util.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.bocchi.bocchiweb.dto.order.OrderDTO;
import com.bocchi.bocchiweb.dto.order.OrderDetailDTO;
import com.bocchi.bocchiweb.entity.Order;
import com.bocchi.bocchiweb.entity.OrderDetail;
import com.bocchi.bocchiweb.entity.Product;

public class OrderMapper {
    public static OrderDetailDTO toOrderDetailDTO(OrderDetail detail) {
        Product product = detail.getProduct();
        return OrderDetailDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImgUrl())
                .quantity(detail.getQuantity())
                .price(detail.getPrice())
                .build();
    }

    public static OrderDTO toOrderDTO(Order order) {
        List<OrderDetailDTO> detailDTOs = order.getOrderDetails().stream()
                .map(OrderMapper::toOrderDetailDTO)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .recipientName(order.getRecipientName())
                .recipientPhone(order.getRecipientPhone())
                .status(order.getStatus())
                .email(order.getUser().getEmail())
                .orderDate(order.getOrderDate())
                .items(detailDTOs)
                .build();
    }
}
