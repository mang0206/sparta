package com.sparta.demo.domain.product.repository;

import com.sparta.demo.domain.product.dto.ProductResponse;
import com.sparta.demo.domain.product.dto.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductResponse> searchProducts(ProductSearchCondition condition, Pageable pageable);
}
