package com.sparta.demo.domain.product.service;

import com.sparta.demo.common.ServiceException;
import com.sparta.demo.common.ServiceExceptionCode;
import com.sparta.demo.domain.category.entity.Category;
import com.sparta.demo.domain.category.repository.CategoryRepository;
import com.sparta.demo.domain.product.dto.ProductRequest;
import com.sparta.demo.domain.product.dto.ProductResponse;
import com.sparta.demo.domain.product.dto.ProductSearchCondition;
import com.sparta.demo.domain.product.entity.Product;
import com.sparta.demo.domain.product.repository.ProductRepository;
import com.sparta.demo.domain.product.repository.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 1. 상품 등록
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        // 카테고리 조회
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category) // 조회한 Category 엔티티 연결
                .build();

        Product savedProduct = productRepository.save(product);
        return new ProductResponse(savedProduct);
    }

    // 2-1. 단일 상품 조회
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID입니다."));
        return new ProductResponse(product);
    }

    // 2-2. 상품 목록 조회 (검색 및 필터링)
    public Page<ProductResponse> searchProducts(ProductSearchCondition condition, Pageable pageable) {
        // Specification 생성
        Specification<Product> spec = ProductSpecification.search(condition);

        // Specification과 Pageable을 사용하여 조회
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        // Page<Product> -> Page<ProductResponse>
        return productPage.map(ProductResponse::new);
    }

    // 3. 상품 수정
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID입니다."));

        // 카테고리 ID가 변경되었는지 확인
        Category category = product.getCategory();
        if (!category.getId().equals(request.getCategoryId())) {
            // 카테고리가 변경된 경우, 새로 조회
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));
        }

        // 엔티티 수정 (더티 체킹)
        product.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                category
        );

        return new ProductResponse(product);
    }
}
