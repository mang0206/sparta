package com.sparta.msa.project_part_3.domain.coupon.dto.response;

import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUserStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponUserResponse {
    Long id;
    Long couponId;
    String couponName;
    String code;
    CouponUserStatus status;

    public static CouponUserResponse from(CouponUser couponUser) {
        return CouponUserResponse.builder()
                .id(couponUser.getId())
                .couponId(couponUser.getCoupon().getId())
                .couponName(couponUser.getCoupon().getCouponName())
                .code(couponUser.getCode())
                .status(couponUser.getStatus())
                .build();
    }
}