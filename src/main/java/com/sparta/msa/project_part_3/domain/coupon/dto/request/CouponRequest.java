package com.sparta.msa.project_part_3.domain.coupon.dto.request;

import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponRequest {

    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    String couponName;

    @NotNull(message = "할인 유형은 필수입니다.")
    DiscountType discountType;

    @Min(value = 1, message = "할인 값은 0보다 커야 합니다.")
    BigDecimal discountValue;

    @Min(value = 0, message = "최소 주문 금액은 0 이상이어야 합니다.")
    BigDecimal minOrderAmount;

    BigDecimal maxDiscountAmount;

    @NotNull(message = "시작 날짜는 필수입니다.")
    LocalDateTime startDate;

    @NotNull(message = "종료 날짜는 필수입니다.")
    @Future(message = "종료 날짜는 미래여야 합니다.")
    LocalDateTime endDate;

    @Min(value = 0, message = "발급 한도는 0 이상이어야 합니다.")
    Integer usageLimit;
}