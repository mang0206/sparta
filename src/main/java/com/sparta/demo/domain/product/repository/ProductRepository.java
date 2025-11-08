package com.sparta.demo.domain.product.repository;

import com.sparta.demo.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
    // JpaSpecificationExecutor: Specification(명세)을 사용한 동적 쿼리 실행 인터페이스
}

