package com.bocchi.bocchiweb.dto.cart;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {
    private List<CartItemDTO> items;
    private Integer totalQuantity;
    private BigDecimal totalPrice;
}
