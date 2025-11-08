package com.sparta.demo.domain.refund.dto;

import com.sparta.demo.domain.refund.entity.RefundStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefundProcessRequest {

    @Schema(description = "변경할 환불 상태 (APPROVED 또는 REJECTED)", example = "APPROVED")
    @NotNull(message = "환불 처리 상태는 필수입니다.")
    private RefundStatus status; // APPROVED 또는 REJECTED만 받아야 함

    // (개선) @EnumValidator 같은 커스텀 어노테이션으로 PENDING이 들어오는 것을 막을 수 있습니다.
}