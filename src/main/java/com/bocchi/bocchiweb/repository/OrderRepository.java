package com.bocchi.bocchiweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bocchi.bocchiweb.entity.Order;
import com.bocchi.bocchiweb.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByIdAndUser(Long id, User user);

    @Query("""
                SELECT CASE WHEN COUNT(od) > 0 THEN true ELSE false END
                FROM Order o
                JOIN o.orderDetails od
                WHERE o.user.id = :userId
                AND od.product.id = :productId
                AND o.status = 'COMPLETED'
            """)
    boolean existsCompletedOrderContainingProduct(Long userId, Long productId);
}
