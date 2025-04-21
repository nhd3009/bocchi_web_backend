package com.bocchi.bocchiweb.util.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.bocchi.bocchiweb.dto.cart.CartItemDTO;
import com.bocchi.bocchiweb.dto.cart.CartResponseDTO;
import com.bocchi.bocchiweb.entity.CartItem;
import com.bocchi.bocchiweb.entity.Product;

public class CartMapper {
    public static CartItemDTO toDTO(CartItem item) {
        Product p = item.getProduct();
        return new CartItemDTO(
                p.getId(),
                p.getName(),
                p.getImgUrl(),
                p.getPrice(),
                item.getQuantity(),
                p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
    }

    public static CartResponseDTO toCartResponse(List<CartItem> items) {
        List<CartItemDTO> dtos = items.stream()
                .map(CartMapper::toDTO)
                .collect(Collectors.toList());

        int totalQuantity = items.stream().mapToInt(CartItem::getQuantity).sum();
        BigDecimal totalPrice = items.stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(dtos, totalQuantity, totalPrice);
    }
}
