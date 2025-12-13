package com.sparta.msa.project_part_3.domain.coupon.dto.response;

import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponseDto {
    final Long id;
    final String couponName;
    final DiscountType discountType;
    final Long discountValue;
    final Long minOrderAmount;
    final Long maxDiscountAmount;
    final LocalDateTime startDate;
    final LocalDateTime endDate;
    final Integer usageLimit;
    final Integer issueCount;
    final Integer usedCount;
    final LocalDateTime createdAt;
    final LocalDateTime updatedAt;

    public CouponResponseDto(Coupon coupon) {
        this.id = coupon.getId();
        this.couponName = coupon.getCouponName();
        this.discountType = coupon.getDiscountType();
        this.discountValue = coupon.getDiscountValue();
        this.minOrderAmount = coupon.getMinOrderAmount();
        this.maxDiscountAmount = coupon.getMaxDiscountAmount();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.usageLimit = coupon.getUsageLimit();
        this.issueCount = coupon.getIssueCount();
        this.usedCount = coupon.getUsedCount();
        this.createdAt = coupon.getCreatedAt();
        this.updatedAt = coupon.getUpdatedAt();
    }
}
