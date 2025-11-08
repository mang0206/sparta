package com.sparta.demo.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter // Controller에서 @ModelAttribute로 받기 위해 Setter 필요
public class ProductSearchCondition {

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "최소 가격", example = "10000")
    private BigDecimal minPrice;

    @Schema(description = "최대 가격", example = "50000")
    private BigDecimal maxPrice;

    @Schema(description = "상품명 키워드", example = "마우스")
    private String keyword;
}