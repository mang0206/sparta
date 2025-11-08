package com.sparta.demo.domain.refund.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefundRequest {

    @Schema(description = "회원 ID", example = "1")
    @NotNull(message = "회원 ID는 필수입니다.")
    private Long userId;

    @Schema(description = "구매 ID", example = "1")
    @NotNull(message = "구매 ID는 필수입니다.")
    private Long purchaseId;

    @Schema(description = "환불 사유", example = "상품 파손")
    @NotBlank(message = "환불 사유는 필수입니다.")
    private String reason;
}