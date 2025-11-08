package com.sparta.demo.domain.Purcahse.Controller;

import com.sparta.demo.domain.Purcahse.Service.PurchaseService;
import com.sparta.demo.domain.Purcahse.dto.PurchaseRequest;
import com.sparta.demo.domain.Purcahse.dto.PurchaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Purchase", description = "구매 관리 API") // "Order" -> "Purchase"
@RestController
@RequestMapping("/api/purchases") // "/api/orders" -> "/api/purchases"
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Operation(summary = "구매 생성", description = "상품을 구매합니다. (재고 차감)")
    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(
        @Valid @RequestBody PurchaseRequest request
    ) {
        PurchaseResponse response = purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "사용자 구매 목록 조회", description = "특정 사용자의 구매 목록을 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByUserId(
        @PathVariable Long userId
    ) {
        List<PurchaseResponse> responseList = purchaseService.getPurchasesByUserId(userId);
        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "구매 상태 변경 (완료)",
            description = "구매 상태를 'PENDING'에서 'COMPLETED'로 변경합니다.")
    @PatchMapping("/{purchaseId}/complete")
    public ResponseEntity<PurchaseResponse> completePurchase(
      @PathVariable Long purchaseId
    ) {
        PurchaseResponse response = purchaseService.completePurchase(purchaseId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "구매 취소",
            description = "'PENDING' 상태의 구매를 취소합니다. (재고 복원)")
    @PostMapping("/{purchaseId}/cancel")
    public ResponseEntity<PurchaseResponse> cancelPurchase(
        @PathVariable Long purchaseId
    ) {
        PurchaseResponse response = purchaseService.cancelPurchase(purchaseId);
        return ResponseEntity.ok(response);
    }
}
