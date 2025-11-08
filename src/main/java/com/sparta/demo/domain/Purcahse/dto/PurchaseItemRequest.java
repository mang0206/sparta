package com.sparta.demo.domain.Purcahse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseItemRequest {
    @Schema(description = "상품 ID", example = "1")
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @Schema(description = "구매 수량", example = "2")
    @Min(value = 1, message = "구매 수량은 1 이상이어야 합니다.")
    private int quantity;
}