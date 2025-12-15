package com.sparta.msa.project_part_3.domain.coupon.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfflineCouponRequest {
    @NotNull
    Long couponId;

    @NotNull
    @Min(1)
    Integer count;
}
