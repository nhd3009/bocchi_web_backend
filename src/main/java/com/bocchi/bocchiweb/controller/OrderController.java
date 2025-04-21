package com.bocchi.bocchiweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bocchi.bocchiweb.dto.order.OrderDTO;
import com.bocchi.bocchiweb.service.OrderService;
import com.bocchi.bocchiweb.util.request.order.CreateOrderRequest;
import com.bocchi.bocchiweb.util.request.order.OrderFilterRequest;
import com.bocchi.bocchiweb.util.request.order.UpdateOrderRequest;
import com.bocchi.bocchiweb.util.request.order.UpdateOrderStatusRequest;
import com.bocchi.bocchiweb.util.response.ApiMessage;
import com.bocchi.bocchiweb.util.response.ApiResponse;
import com.bocchi.bocchiweb.util.response.PageResponse;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Create Order")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@RequestBody CreateOrderRequest request) {
        OrderDTO orderDTO = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, null, "Order placed successfully", orderDTO));
    }

    @GetMapping("/orders/my")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Get My Orders")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getMyOrders(@ModelAttribute OrderFilterRequest request) {
        PageResponse<OrderDTO> orders = orderService.getMyOrders(request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Success", orders));
    }

    @PutMapping("/orders/my/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<OrderDTO>> updateMyOrder(@PathVariable Long id,
            @RequestBody UpdateOrderRequest request) {
        OrderDTO result = orderService.updateMyOrder(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Your order has been updated!", result));
    }

    @PutMapping("/orders/my/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> cancelMyOrder(@PathVariable Long id) {
        orderService.cancelMyOrder(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Your order has been cancelled!", null));
    }

    @PutMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(@PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request) {
        OrderDTO result = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Order has been updated!", result));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiMessage("Get All Orders")
    public ResponseEntity<ApiResponse<PageResponse<OrderDTO>>> getAllOrders(
            @ModelAttribute OrderFilterRequest request) {
        PageResponse<OrderDTO> orders = orderService.getAllOrdersAsAdmin(request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Success", orders));
    }

    @GetMapping("/orders/my/{id}")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Get My Order Detail")
    public ResponseEntity<ApiResponse<OrderDTO>> getMyOrderDetail(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getMyOrderDetail(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Get My Order Successful!", orderDTO));
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiMessage("Get Order Detail by Admin")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderDetail(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderDetail(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Get Detail Order Successful!", orderDTO));
    }

}
