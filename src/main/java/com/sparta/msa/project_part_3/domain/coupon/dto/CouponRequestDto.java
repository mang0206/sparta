package com.sparta.msa.project_part_3.domain.coupon.dto;

import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponRequestDto {

    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    private String couponName;

    @NotNull(message = "할인 유형은 필수입니다.")
    private DiscountType discountType;

    @Min(value = 1, message = "할인 값은 0보다 커야 합니다.")
    private Long discountValue;

    @Min(value = 0, message = "최소 주문 금액은 0 이상이어야 합니다.")
    private Long minOrderAmount;

    private Long maxDiscountAmount;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDateTime startDate;

    @NotNull(message = "종료 날짜는 필수입니다.")
    @Future(message = "종료 날짜는 미래여야 합니다.")
    private LocalDateTime endDate;

    @Min(value = 0, message = "발급 한도는 0 이상이어야 합니다.")
    private Integer usageLimit;
}
