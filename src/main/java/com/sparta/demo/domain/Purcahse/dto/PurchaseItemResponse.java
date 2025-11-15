package com.sparta.demo.domain.Purcahse.dto;

import com.sparta.demo.domain.Purcahse.entity.PurchaseItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
public class PurchaseItemResponse {
    @Schema(description = "상품 ID")
    private Long productId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "주문 수량")
    private int quantity;

    @Schema(description = "구매 시점 가격")
    private BigDecimal price; // priceAtOrder -> priceAtPurchase

    public PurchaseItemResponse(PurchaseItem item) { // OrderItem -> PurchaseItem
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.quantity = item.getQuantity();
        this.price = item.getPrice(); // '구매' 시점 가격
    }
}