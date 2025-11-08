package com.sparta.demo.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @Schema(description = "상품명", example = "게이밍 마우스")
    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @Schema(description = "상품 설명", example = "고성능 옵티컬 센서 탑재")
    private String description;

    @Schema(description = "가격", example = "59000")
    @NotNull(message = "가격은 필수입니다.")
    @Positive(message = "가격은 양수여야 합니다")
    private BigDecimal price;

    @Schema(description = "재고", example = "100")
    @NotNull(message = "재고는 필수입니다.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    @Schema(description = "카테고리 ID", example = "1")
    @NotNull(message = "카테고리 ID는 필수입니다.")
    private Long categoryId;
}