package com.sparta.demo.domain.product.repository;

import com.sparta.demo.domain.product.dto.ProductSearchCondition;
import com.sparta.demo.domain.product.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> search(ProductSearchCondition condition) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 카테고리 ID
            if (condition.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), condition.getCategoryId()));
            }

            // 2. 가격 범위 (minPrice ~ maxPrice)
            if (condition.getMinPrice() != null && condition.getMaxPrice() != null) {
                predicates.add(cb.between(root.get("price"), condition.getMinPrice(), condition.getMaxPrice()));
            } else if (condition.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), condition.getMinPrice()));
            } else if (condition.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), condition.getMaxPrice()));
            }

            // 3. 상품명 키워드 (Like 검색)
            if (StringUtils.hasText(condition.getKeyword())) {
                predicates.add(cb.like(root.get("name"), "%" + condition.getKeyword() + "%"));
            }

            // 모든 조건을 AND로 결합
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}