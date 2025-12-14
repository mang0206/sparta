package com.sparta.msa.project_part_3.domain.coupon.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponRegistrationRequest {
    String couponCode;
    Long userId; // Note: In a real app, userId usually comes from security context, but prompt requested explicit userId in body for this API
}
