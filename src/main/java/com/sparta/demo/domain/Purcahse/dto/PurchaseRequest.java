package com.sparta.demo.domain.Purcahse.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PurchaseRequest { // OrderRequest -> PurchaseRequest

    @Schema(description = "회원(유저) ID", example = "1")
    @NotNull(message = "회원 ID는 필수입니다.")
    private Long userId;

    @Schema(description = "배송 주소", example = "서울시 강남구")
    @NotBlank(message = "배송 주소는 필수입니다.")
    private String shippingAddress;

    @Schema(description = "구매 상품 목록")
    @Valid
    @Size(min = 1, message = "구매 상품은 1개 이상이어야 합니다.")
    private List<PurchaseItemRequest> items; // OrderItemRequest -> PurchaseItemRequest
}