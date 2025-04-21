package com.bocchi.bocchiweb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bocchi.bocchiweb.dto.review.ReviewDTO;
import com.bocchi.bocchiweb.entity.Product;
import com.bocchi.bocchiweb.entity.Review;
import com.bocchi.bocchiweb.entity.User;
import com.bocchi.bocchiweb.exception.BadRequestException;
import com.bocchi.bocchiweb.exception.ForbiddenException;
import com.bocchi.bocchiweb.exception.ResourceNotFoundException;
import com.bocchi.bocchiweb.repository.OrderRepository;
import com.bocchi.bocchiweb.repository.ProductRepository;
import com.bocchi.bocchiweb.repository.ReviewRepository;
import com.bocchi.bocchiweb.repository.UserRepository;
import com.bocchi.bocchiweb.util.mapper.ReviewMapper;
import com.bocchi.bocchiweb.util.request.review.CreateReviewRequest;
import com.bocchi.bocchiweb.util.request.review.ReviewFilterRequest;
import com.bocchi.bocchiweb.util.request.review.UpdateReviewRequest;
import com.bocchi.bocchiweb.util.response.PageResponse;
import com.bocchi.bocchiweb.util.specification.ReviewSpecification;

@Service
public class ReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(UserRepository userRepository, ProductRepository productRepository,
            OrderRepository orderRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    public ReviewDTO createReview(CreateReviewRequest request) {
        String email = AuthService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        boolean hasPurchased = orderRepository.existsCompletedOrderContainingProduct(user.getId(), product.getId());
        if (!hasPurchased) {
            throw new BadRequestException("You can only review products you’ve purchased in completed orders");
        }

        if (reviewRepository.existsByUserAndProduct(user, product)) {
            throw new BadRequestException("You have already reviewed this product");
        }

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setProduct(product);
        review.setUser(user);

        review = reviewRepository.save(review);
        return ReviewMapper.toDTO(review);
    }

    public PageResponse<ReviewDTO> getReviewsByProduct(Long productId, ReviewFilterRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());
        Page<Review> page = reviewRepository.findByProduct(product, pageable);
        List<ReviewDTO> dtos = page.stream().map(ReviewMapper::toDTO).toList();

        return new PageResponse<>(dtos, page.getNumber(), page.getSize(), page.getTotalPages(),
                page.getTotalElements());
    }

    public PageResponse<ReviewDTO> getAllReviews(ReviewFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());
        Specification<Review> spec = ReviewSpecification.filter(request);

        Page<Review> page = reviewRepository.findAll(spec, pageable);
        List<ReviewDTO> dtoList = page.getContent().stream()
                .map(ReviewMapper::toDTO)
                .toList();

        return new PageResponse<>(dtoList, page.getNumber(), page.getSize(), page.getTotalPages(),
                page.getTotalElements());
    }

    public ReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) {
        String email = AuthService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You can only update your own review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review = reviewRepository.save(review);

        return ReviewMapper.toDTO(review);
    }

    public void deleteReview(Long reviewId) {
        String email = AuthService.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        boolean isAdmin = AuthService.hasRole("ADMIN");
        boolean isOwner = review.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new ForbiddenException("You are not allowed to delete this review");
        }

        reviewRepository.delete(review);
    }
}
