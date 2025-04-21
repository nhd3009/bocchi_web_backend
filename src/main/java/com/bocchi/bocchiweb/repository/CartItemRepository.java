package com.bocchi.bocchiweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bocchi.bocchiweb.entity.Cart;
import com.bocchi.bocchiweb.entity.CartItem;
import com.bocchi.bocchiweb.entity.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    List<CartItem> findByCart(Cart cart);
}
