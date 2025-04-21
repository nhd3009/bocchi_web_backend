package com.bocchi.bocchiweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bocchi.bocchiweb.dto.cart.CartResponseDTO;
import com.bocchi.bocchiweb.service.CartService;
import com.bocchi.bocchiweb.util.request.cart.AddToCartRequest;
import com.bocchi.bocchiweb.util.request.cart.UpdateListCartItemsRequest;
import com.bocchi.bocchiweb.util.response.ApiMessage;
import com.bocchi.bocchiweb.util.response.ApiResponse;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Add item to cart")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(@RequestBody AddToCartRequest request) {
        CartResponseDTO cart = cartService.addToCart(request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Added to cart successfully!", cart));
    }

    @PutMapping("/cart/items")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Update multiple cart items")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateCartItems(
            @RequestBody UpdateListCartItemsRequest request) {
        CartResponseDTO dto = cartService.updateCartItems(request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Cart updated successfully!", dto));
    }

    @DeleteMapping("/cart/items/{productId}")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Remove Cart Item")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeCartItem(@PathVariable Long productId) {
        CartResponseDTO response = cartService.removeCartItem(productId);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Removed item from cart successfully!", response));
    }

    @GetMapping("/cart")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Get My Cart")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getMyCart() {
        CartResponseDTO response = cartService.getMyCart();
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Fetched cart successfully!", response));
    }
}
