package com.sparta.demo.domain.product.dto;

import com.sparta.demo.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class ProductResponse {

    @Schema(description = "상품 ID")
    private Long id;
    @Schema(description = "상품명")
    private String name;
    @Schema(description = "상품 설명")
    private String description;
    @Schema(description = "가격")
    private BigDecimal price;
    @Schema(description = "재고")
    private Integer stock;
    @Schema(description = "카테고리 ID")
    private Long categoryId;
    @Schema(description = "카테고리명")
    private String categoryName;

    // Entity to DTO
    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        // 카테고리가 null일 수 있는 경우를 대비 (방어적 코딩)
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
            this.categoryName = product.getCategory().getName();
        }
    }
}