package com.sparta.msa.project_part_3.domain.product.repository;

import com.sparta.msa.project_part_3.domain.product.dto.request.ProductSearchCondition;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchProducts(ProductSearchCondition condition, Pageable pageable);
}
