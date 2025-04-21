package com.bocchi.bocchiweb.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bocchi.bocchiweb.dto.order.OrderDTO;
import com.bocchi.bocchiweb.entity.Cart;
import com.bocchi.bocchiweb.entity.CartItem;
import com.bocchi.bocchiweb.entity.Order;
import com.bocchi.bocchiweb.entity.OrderDetail;
import com.bocchi.bocchiweb.entity.Product;
import com.bocchi.bocchiweb.entity.User;
import com.bocchi.bocchiweb.exception.BadRequestException;
import com.bocchi.bocchiweb.exception.ForbiddenException;
import com.bocchi.bocchiweb.exception.ResourceNotFoundException;
import com.bocchi.bocchiweb.repository.CartItemRepository;
import com.bocchi.bocchiweb.repository.CartRepository;
import com.bocchi.bocchiweb.repository.OrderDetailRepository;
import com.bocchi.bocchiweb.repository.OrderRepository;
import com.bocchi.bocchiweb.repository.ProductRepository;
import com.bocchi.bocchiweb.repository.UserRepository;
import com.bocchi.bocchiweb.util.enums.OrderStatus;
import com.bocchi.bocchiweb.util.mapper.OrderMapper;
import com.bocchi.bocchiweb.util.request.order.CreateOrderRequest;
import com.bocchi.bocchiweb.util.request.order.OrderFilterRequest;
import com.bocchi.bocchiweb.util.request.order.UpdateOrderRequest;
import com.bocchi.bocchiweb.util.request.order.UpdateOrderStatusRequest;
import com.bocchi.bocchiweb.util.response.PageResponse;
import com.bocchi.bocchiweb.util.specification.OrderSpecification;

@Service
public class OrderService {
        private final UserRepository userRepository;
        private final CartRepository cartRepository;
        private final CartItemRepository cartItemRepository;
        private final OrderRepository orderRepository;
        private final OrderDetailRepository orderDetailRepository;
        private final ProductRepository productRepository;

        public OrderService(UserRepository userRepository, CartRepository cartRepository,
                        CartItemRepository cartItemRepository, OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository, ProductRepository productRepository) {
                this.userRepository = userRepository;
                this.cartItemRepository = cartItemRepository;
                this.cartRepository = cartRepository;
                this.orderRepository = orderRepository;
                this.orderDetailRepository = orderDetailRepository;
                this.productRepository = productRepository;
        }

        public OrderDTO createOrder(CreateOrderRequest request) {
                String email = AuthService.getCurrentUserEmail();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Cart cart = cartRepository.findByUser(user)
                                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

                List<CartItem> cartItems = cartItemRepository.findByCart(cart);
                if (cartItems.isEmpty()) {
                        throw new BadRequestException("Cart is empty");
                }

                for (CartItem item : cartItems) {
                        Product product = item.getProduct();
                        if (product.getStock() < item.getQuantity()) {
                                throw new BadRequestException("Not enough stock for product: " + product.getName());
                        }
                }

                Order order = new Order();
                order.setUser(user);
                order.setRecipientName(request.getRecipientName());
                order.setRecipientPhone(request.getRecipientPhone());
                order.setShippingAddress(request.getAddress());
                order.setOrderDate(LocalDateTime.now());
                order.setStatus(OrderStatus.PENDING);
                order = orderRepository.save(order);

                BigDecimal totalAmount = BigDecimal.ZERO;

                for (CartItem item : cartItems) {
                        Product product = item.getProduct();

                        product.setStock(product.getStock() - item.getQuantity());
                        productRepository.save(product);

                        OrderDetail detail = new OrderDetail();
                        detail.setOrder(order);
                        detail.setProduct(product);
                        detail.setQuantity(item.getQuantity());
                        detail.setPrice(product.getPrice());
                        order.getOrderDetails().add(detail);
                        orderDetailRepository.save(detail);

                        totalAmount = totalAmount
                                        .add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }

                order.setTotalAmount(totalAmount);
                order = orderRepository.save(order);

                cartItemRepository.deleteAll(cartItems);

                return OrderMapper.toOrderDTO(order);
        }

        // public Page<OrderDTO> getMyOrders(OrderFilterRequest request) {
        // String email = AuthService.getCurrentUserEmail();
        // User user = userRepository.findByEmail(email)
        // .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Pageable pageable = PageRequest.of(request.getPage(), request.getSize(),
        // Sort.by("orderDate").descending());

        // Page<Order> orders =
        // orderRepository.findAll(OrderSpecification.filter(request, user), pageable);

        // return orders.map(OrderMapper::toOrderDTO);
        // }

        public OrderDTO updateMyOrder(Long orderId, UpdateOrderRequest request) {
                String email = AuthService.getCurrentUserEmail();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

                if (!order.getUser().getId().equals(user.getId())) {
                        throw new ForbiddenException("You are not allowed to update this order");
                }

                if (!order.getStatus().equals(OrderStatus.PENDING)) {
                        throw new BadRequestException("Only pending orders can be updated");
                }

                order.setRecipientName(request.getRecipientName());
                order.setRecipientPhone(request.getRecipientPhone());
                order.setShippingAddress(request.getShippingAddress());

                orderRepository.save(order);
                return OrderMapper.toOrderDTO(order);
        }

        public void cancelMyOrder(Long orderId) {
                String email = AuthService.getCurrentUserEmail();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

                if (!order.getUser().getId().equals(user.getId())) {
                        throw new ForbiddenException("You are not allowed to cancel this order");
                }

                if (!order.getStatus().equals(OrderStatus.PENDING)) {
                        throw new BadRequestException("Only pending orders can be canceled");
                }

                for (OrderDetail detail : order.getOrderDetails()) {
                        Product product = detail.getProduct();
                        product.setStock(product.getStock() + detail.getQuantity());
                        productRepository.save(product);
                }

                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
        }

        public OrderDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

                OrderStatus currentStatus = order.getStatus();
                OrderStatus newStatus = request.getStatus();

                if (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.CANCELLED) {
                        for (OrderDetail detail : order.getOrderDetails()) {
                                Product product = detail.getProduct();
                                product.setStock(product.getStock() + detail.getQuantity());
                                productRepository.save(product);
                        }
                }

                order.setStatus(newStatus);
                orderRepository.save(order);

                return OrderMapper.toOrderDTO(order);
        }

        public PageResponse<OrderDTO> getMyOrders(OrderFilterRequest request) {
                String currentEmail = AuthService.getCurrentUserEmail();
                User currentUser = userRepository.findByEmail(currentEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Pageable pageable = PageRequest.of(request.getPage(), request.getSize(),
                                Sort.by("orderDate").descending());
                Specification<Order> spec = OrderSpecification.filter(request, currentUser);

                Page<Order> page = orderRepository.findAll(spec, pageable);
                List<OrderDTO> dtos = page.getContent().stream().map(OrderMapper::toOrderDTO).toList();

                return new PageResponse<>(dtos, page.getNumber(), page.getSize(), page.getTotalPages(),
                                page.getTotalElements());
        }

        public PageResponse<OrderDTO> getAllOrdersAsAdmin(OrderFilterRequest request) {
                Pageable pageable = PageRequest.of(request.getPage(), request.getSize(),
                                Sort.by("orderDate").descending());
                Specification<Order> spec = OrderSpecification.filter(request, null);

                Page<Order> page = orderRepository.findAll(spec, pageable);
                List<OrderDTO> dtos = page.getContent().stream().map(OrderMapper::toOrderDTO).toList();

                return new PageResponse<>(dtos, page.getNumber(), page.getSize(), page.getTotalPages(),
                                page.getTotalElements());
        }

        public OrderDTO getMyOrderDetail(Long orderId) {
                String email = AuthService.getCurrentUserEmail();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                Order order = orderRepository.findByIdAndUser(orderId, user)
                                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this user"));

                return OrderMapper.toOrderDTO(order);
        }

        public OrderDTO getOrderDetail(Long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

                return OrderMapper.toOrderDTO(order);
        }
}
