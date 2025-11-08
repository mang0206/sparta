package com.sparta.demo.domain.refund.dto;

import com.sparta.demo.domain.refund.entity.Refund;
import com.sparta.demo.domain.refund.entity.RefundStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RefundResponse {
    @Schema(description = "환불 ID")
    private Long refundId;
    @Schema(description = "구매 ID")
    private Long purchaseId;
    @Schema(description = "회원 ID")
    private Long userId;
    @Schema(description = "환불 사유")
    private String reason;
    @Schema(description = "환불 상태")
    private RefundStatus status;
    @Schema(description = "환불 요청 일시")
    private LocalDateTime requestedAt;
    @Schema(description = "환불 처리 일시")
    private LocalDateTime processedAt;

    public RefundResponse(Refund refund) {
        this.refundId = refund.getId();
        this.purchaseId = refund.getPurchase().getId();
        this.userId = refund.getUser().getId();
        this.reason = refund.getReason();
        this.status = refund.getStatus();
        this.requestedAt = refund.getRequestedAt();
        this.processedAt = refund.getProcessedAt();
    }
}