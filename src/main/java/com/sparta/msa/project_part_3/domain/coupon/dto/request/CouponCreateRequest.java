package com.sparta.msa.project_part_3.domain.coupon.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCreateRequest {
    String name;
    BigDecimal discount;
    LocalDateTime validFrom;
    LocalDateTime validTo;
}
