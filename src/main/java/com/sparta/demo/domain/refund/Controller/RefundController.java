package com.sparta.demo.domain.refund.Controller;

import com.sparta.demo.domain.refund.dto.RefundRequest;
import com.sparta.demo.domain.refund.dto.RefundResponse;
import com.sparta.demo.domain.refund.entity.RefundStatus;
import com.sparta.demo.domain.refund.service.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Refund", description = "환불 관리 API")
@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @Operation(summary = "환불 요청", description = "주문에 대한 환불을 요청합니다.")
    @PostMapping
    public ResponseEntity<RefundResponse> requestRefund(
            @Valid @RequestBody RefundRequest request
    ) {
        RefundResponse response = refundService.requestRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "환불 처리", description = "환불 요청을 승인 또는 거절합니다.")
    @PatchMapping("/{refundId}/process")
    public ResponseEntity<Void> processRefund(
            @PathVariable Long refundId,
            @RequestParam RefundStatus status
    ) {
        refundService.processRefund(refundId, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자별 환불 목록 조회", description = "특정 사용자의 환불 목록을 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RefundResponse>> getRefundsByUser(@PathVariable Long userId) {
        List<RefundResponse> response = refundService.getRefundsByUser(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 환불 목록 조회 (관리자)", description = "전체 환불 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<RefundResponse>> getAllRefunds() {
        List<RefundResponse> response = refundService.getAllRefunds();
        return ResponseEntity.ok(response);
    }
}
