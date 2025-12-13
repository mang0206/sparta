package com.sparta.msa.project_part_3.domain.coupon.dto.response;

import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponse {
    Long id;
    String name;
    BigDecimal discount;
    String code;
    String status;
    LocalDateTime validFrom;
    LocalDateTime validTo;

    public static CouponResponse from(CouponUser couponUser) {
        return new CouponResponse(
            couponUser.getId(),
            couponUser.getCoupon().getName(),
            couponUser.getCoupon().getDiscount(),
            couponUser.getCode(),
            couponUser.getStatus().name(),
            couponUser.getCoupon().getValidFrom(),
            couponUser.getCoupon().getValidTo()
        );
    }
}
