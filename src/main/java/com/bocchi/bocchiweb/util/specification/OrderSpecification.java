package com.bocchi.bocchiweb.util.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.bocchi.bocchiweb.entity.Order;
import com.bocchi.bocchiweb.entity.User;
import com.bocchi.bocchiweb.util.enums.OrderStatus;
import com.bocchi.bocchiweb.util.request.order.OrderFilterRequest;

public class OrderSpecification {

    public static Specification<Order> filter(OrderFilterRequest request, User currentUser) {
        return Specification
                .where(filterByEmailOrUser(request.getEmail(), currentUser))
                .and(statusEquals(request.getStatus()))
                .and(recipientNameContains(request.getRecipientName()))
                .and(dateAfterOrEqual(request.getStartDate()))
                .and(dateBeforeOrEqual(request.getEndDate()));
    }

    private static Specification<Order> filterByEmailOrUser(String email, User user) {
        return (root, query, cb) -> {
            if (email != null && !email.isBlank()) {
                return cb.equal(root.get("user").get("email"), email);
            }
            if (user != null) {
                return cb.equal(root.get("user"), user);
            }
            return null;
        };
    }

    private static Specification<Order> statusEquals(OrderStatus status) {
        return (root, query, cb) -> {
            if (status == null)
                return null;
            return cb.equal(root.get("status"), status);
        };
    }

    private static Specification<Order> recipientNameContains(String recipientName) {
        return (root, query, cb) -> {
            if (recipientName == null || recipientName.isBlank())
                return null;
            return cb.like(cb.lower(root.get("recipientName")), "%" + recipientName.toLowerCase() + "%");
        };
    }

    private static Specification<Order> dateAfterOrEqual(LocalDate startDate) {
        return (root, query, cb) -> {
            if (startDate == null)
                return null;
            return cb.greaterThanOrEqualTo(root.get("orderDate"), startDate.atStartOfDay());
        };
    }

    private static Specification<Order> dateBeforeOrEqual(LocalDate endDate) {
        return (root, query, cb) -> {
            if (endDate == null)
                return null;
            return cb.lessThanOrEqualTo(root.get("orderDate"), endDate.atTime(23, 59, 59));
        };
    }
}
