package com.bocchi.bocchiweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bocchi.bocchiweb.dto.product.ProductDTO;
import com.bocchi.bocchiweb.service.ProductService;
import com.bocchi.bocchiweb.util.request.product.CreateProductRequest;
import com.bocchi.bocchiweb.util.request.product.ProductFilterRequest;
import com.bocchi.bocchiweb.util.request.product.UpdateProductRequest;
import com.bocchi.bocchiweb.util.response.ApiMessage;
import com.bocchi.bocchiweb.util.response.ApiResponse;
import com.bocchi.bocchiweb.util.response.PageResponse;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiMessage("Create Product")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @RequestPart("product") CreateProductRequest request,
            @RequestPart("image") MultipartFile image) {

        ProductDTO productDTO = productService.createProduct(request, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, null, "Create product successful!", productDTO));
    }

    @PutMapping(value = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiMessage("Update Product")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") UpdateProductRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        ProductDTO productDTO = productService.updateProduct(id, request, image);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Update product successful!", productDTO));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiMessage("Delete Product")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Delete product successful!", null));
    }

    @GetMapping("/public/products")
    @ApiMessage("Get All Products")
    public PageResponse<ProductDTO> getAllProducts(ProductFilterRequest request) {
        return productService.getAllProducts(request);
    }

    @GetMapping("/public/products/{id}")
    @ApiMessage("Get Product By Id")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        ProductDTO dto = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Get product successful!", dto));
    }
}
