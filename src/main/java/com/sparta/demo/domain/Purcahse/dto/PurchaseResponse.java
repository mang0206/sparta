package com.sparta.demo.domain.Purcahse.dto;

import com.sparta.demo.domain.Purcahse.entity.Purchase;
import com.sparta.demo.domain.Purcahse.entity.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PurchaseResponse {
    @Schema(description = "구매 ID")
    private Long purchaseId;
    @Schema(description = "회원 ID")
    private Long userId;
    @Schema(description = "배송 주소")
    private String shippingAddress;
    @Schema(description = "구매 상태")
    private PurchaseStatus status;
    @Schema(description = "총 구매 금액")
    private BigDecimal totalPrice;
    @Schema(description = "구매 일시")
    private LocalDateTime createdAt;
    @Schema(description = "구매 상품 목록")
    private List<PurchaseItemResponse> items;

    public PurchaseResponse(Purchase purchase) {
        this.purchaseId = purchase.getId();
        this.userId = purchase.getUser().getId();
        this.status = purchase.getStatus();
        this.totalPrice = purchase.getTotalPrice();
        this.createdAt = purchase.getCreatedAt();
        this.items = purchase.getPurchaseItems().stream()
                .map(PurchaseItemResponse::new)
                .collect(Collectors.toList());
    }
}