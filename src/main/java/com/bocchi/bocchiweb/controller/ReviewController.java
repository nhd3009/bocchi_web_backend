package com.bocchi.bocchiweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bocchi.bocchiweb.dto.review.ReviewDTO;
import com.bocchi.bocchiweb.service.ReviewService;
import com.bocchi.bocchiweb.util.request.review.CreateReviewRequest;
import com.bocchi.bocchiweb.util.request.review.ReviewFilterRequest;
import com.bocchi.bocchiweb.util.request.review.UpdateReviewRequest;
import com.bocchi.bocchiweb.util.response.ApiMessage;
import com.bocchi.bocchiweb.util.response.ApiResponse;
import com.bocchi.bocchiweb.util.response.PageResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Create review")
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@RequestBody @Valid CreateReviewRequest request) {
        ReviewDTO dto = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, null, "Created Review Successful", dto));
    }

    @GetMapping("public/reviews/product/{productId}")
    @ApiMessage("Get all reviews for product")
    public ResponseEntity<ApiResponse<PageResponse<ReviewDTO>>> getReviewsByProduct(@PathVariable Long productId,
            @RequestBody ReviewFilterRequest request) {
        PageResponse<ReviewDTO> reviews = reviewService.getReviewsByProduct(productId, request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Get Review By Product Successful!", reviews));
    }

    @PutMapping("/reviews/{id}")
    @PreAuthorize("hasRole('USER')")
    @ApiMessage("Update review")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(@PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request) {
        ReviewDTO dto = reviewService.updateReview(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Review has been updated successful!", dto));
    }

    @DeleteMapping("/reviews/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @ApiMessage("Delete review")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Review has been deleted successful!", null));
    }

    @GetMapping("/admin/reviews")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiMessage("Get all reviews (admin)")
    public ResponseEntity<ApiResponse<PageResponse<ReviewDTO>>> getAllReviews(
            @ModelAttribute ReviewFilterRequest request) {
        PageResponse<ReviewDTO> response = reviewService.getAllReviews(request);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Get All Review Successful!", response));
    }
}
