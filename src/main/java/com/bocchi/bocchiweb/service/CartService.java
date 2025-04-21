package com.bocchi.bocchiweb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bocchi.bocchiweb.dto.cart.CartResponseDTO;
import com.bocchi.bocchiweb.entity.Cart;
import com.bocchi.bocchiweb.entity.CartItem;
import com.bocchi.bocchiweb.entity.Product;
import com.bocchi.bocchiweb.entity.User;
import com.bocchi.bocchiweb.exception.BadRequestException;
import com.bocchi.bocchiweb.exception.ResourceNotFoundException;
import com.bocchi.bocchiweb.repository.CartItemRepository;
import com.bocchi.bocchiweb.repository.CartRepository;
import com.bocchi.bocchiweb.repository.ProductRepository;
import com.bocchi.bocchiweb.repository.UserRepository;
import com.bocchi.bocchiweb.util.mapper.CartMapper;
import com.bocchi.bocchiweb.util.request.cart.AddToCartRequest;
import com.bocchi.bocchiweb.util.request.cart.UpdateCartItemRequest;
import com.bocchi.bocchiweb.util.request.cart.UpdateListCartItemsRequest;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartResponseDTO addToCart(AddToCartRequest request) {
        String email = AuthService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }

        List<CartItem> items = cartItemRepository.findByCart(cart);
        return CartMapper.toCartResponse(items);
    }

    public CartResponseDTO updateCartItems(UpdateListCartItemsRequest request) {
        String email = AuthService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        for (UpdateCartItemRequest itemRequest : request.getItems()) {
            if (itemRequest.getQuantity() < 0) {
                throw new BadRequestException(
                        "Quantity must be greater than or equal to 0 for product ID: " + itemRequest.getProductId());
            }

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cart item not found for product ID: " + product.getId()));

            if (itemRequest.getQuantity() == 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(itemRequest.getQuantity());
                cartItemRepository.save(item);
            }
        }

        List<CartItem> items = cartItemRepository.findByCart(cart);
        return CartMapper.toCartResponse(items);
    }

    public CartResponseDTO removeCartItem(Long productId) {
        String email = AuthService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found for product ID: " + productId));

        cartItemRepository.delete(item);

        List<CartItem> items = cartItemRepository.findByCart(cart);
        return CartMapper.toCartResponse(items);
    }

    public CartResponseDTO getMyCart() {
        String email = AuthService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        List<CartItem> items = cartItemRepository.findByCart(cart);
        return CartMapper.toCartResponse(items);
    }
}
