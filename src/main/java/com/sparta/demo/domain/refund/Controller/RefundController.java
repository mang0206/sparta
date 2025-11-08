package com.sparta.demo.domain.refund.Controller;

import com.sparta.demo.domain.refund.dto.RefundProcessRequest;
import com.sparta.demo.domain.refund.dto.RefundRequest;
import com.sparta.demo.domain.refund.dto.RefundResponse;
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

    @Operation(summary = "환불 요청", description = "'COMPLETED' 상태의 구매 건에 대해 환불을 요청합니다.")
    @PostMapping
    public ResponseEntity<RefundResponse> requestRefund(
            @Valid @RequestBody RefundRequest request
    ) {
        RefundResponse response = refundService.requestRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "환불 처리 (관리자)",
            description = "'PENDING' 상태의 환불 요청을 'APPROVED' 또는 'REJECTED'로 처리합니다. (승인 시 재고 복원)")
    @PatchMapping("/{refundId}/process")
    public ResponseEntity<RefundResponse> processRefund(
            @PathVariable Long refundId,
            @Valid @RequestBody RefundProcessRequest request
    ) {
        RefundResponse response = refundService.processRefund(refundId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 환불 목록 조회", description = "특정 사용자의 환불 요청 목록을 조회합니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RefundResponse>> getRefundsByUserId(
            @PathVariable Long userId
    ) {
        List<RefundResponse> responseList = refundService.getRefundsByUserId(userId);
        return ResponseEntity.ok(responseList);
    }
}