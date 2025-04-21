package com.bocchi.bocchiweb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bocchi.bocchiweb.dto.product.ProductDTO;
import com.bocchi.bocchiweb.entity.Category;
import com.bocchi.bocchiweb.entity.Product;
import com.bocchi.bocchiweb.exception.ApiException;
import com.bocchi.bocchiweb.repository.CategoryRepository;
import com.bocchi.bocchiweb.repository.ProductRepository;
import com.bocchi.bocchiweb.repository.ReviewRepository;
import com.bocchi.bocchiweb.util.error.CategoryErrorCode;
import com.bocchi.bocchiweb.util.error.ProductErrorCode;
import com.bocchi.bocchiweb.util.mapper.ProductMapper;
import com.bocchi.bocchiweb.util.request.product.CreateProductRequest;
import com.bocchi.bocchiweb.util.request.product.ProductFilterRequest;
import com.bocchi.bocchiweb.util.request.product.UpdateProductRequest;
import com.bocchi.bocchiweb.util.response.PageResponse;
import com.bocchi.bocchiweb.util.specification.ProductSpecification;

@Service
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;
    private final ReviewRepository reviewRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository,
            FileStorageService fileStorageService, ReviewRepository reviewRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.fileStorageService = fileStorageService;
        this.reviewRepository = reviewRepository;
    }

    public ProductDTO createProduct(CreateProductRequest request, MultipartFile image) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(CategoryErrorCode.INTERNAL_SERVER_ERROR, "Category not found"));

        String imageUrl = fileStorageService.saveFile(image);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);
        product.setImgUrl(imageUrl);

        Product saved = productRepository.save(product);

        return this.toDTOWithRating(saved);
    }

    public ProductDTO updateProduct(Long id, UpdateProductRequest request, MultipartFile image) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(CategoryErrorCode.INTERNAL_SERVER_ERROR));
        product.setCategory(category);

        if (image != null && !image.isEmpty()) {
            fileStorageService.deleteFile(product.getImgUrl());
            String imageUrl = fileStorageService.saveFile(image);
            product.setImgUrl(imageUrl);
        }

        Product saved = productRepository.save(product);
        return this.toDTOWithRating(saved);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));

        if (product.getImgUrl() != null) {
            fileStorageService.deleteFile(product.getImgUrl());
        }

        productRepository.delete(product);
    }

    public PageResponse<ProductDTO> getAllProducts(ProductFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());
        Specification<Product> spec = ProductSpecification.filter(request);

        Page<Product> page = productRepository.findAll(spec, pageable);
        List<ProductDTO> dtoList = page.getContent().stream()
                .map(this::toDTOWithRating)
                .toList();

        return new PageResponse<>(dtoList, page.getNumber(), page.getSize(), page.getTotalPages(),
                page.getTotalElements());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        return this.toDTOWithRating(product);
    }

    public ProductDTO toDTOWithRating(Product product) {
        ProductDTO dto = ProductMapper.toDTO(product);
        Double avgRating = reviewRepository.getAverageRatingByProductId(product.getId());
        dto.setAverageRating(avgRating != null ? avgRating : 0.0);
        return dto;
    }
}
