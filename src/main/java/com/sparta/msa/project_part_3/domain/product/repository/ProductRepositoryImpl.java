package com.sparta.msa.project_part_3.domain.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.project_part_3.domain.product.dto.request.ProductSearchCondition;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

import static com.sparta.msa.project_part_3.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(ProductSearchCondition condition, Pageable pageable) {
        List<Product> content = queryFactory
                .selectFrom(product)
                .leftJoin(product.category).fetchJoin()
                .where(
                        categoryIdEq(condition.getCategoryId()),
                        isOrderableEq(condition.getIsOrderable())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        categoryIdEq(condition.getCategoryId()),
                        isOrderableEq(condition.getIsOrderable())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression categoryIdEq(String categoryId) {
        return StringUtils.hasText(categoryId) ? product.category.id.eq(Long.valueOf(categoryId)) : null;
    }

    private BooleanExpression isOrderableEq(Boolean isOrderable) {
        if (isOrderable == null) {
            return null;
        }

        return isOrderable ? product.stock.gt(0) : product.stock.loe(0);
    }
}