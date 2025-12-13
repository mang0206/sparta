package com.sparta.msa.project_part_3.domain.coupon.dto;

import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponResponseDto {
    private final Long id;
    private final String couponName;
    private final DiscountType discountType;
    private final Long discountValue;
    private final Long minOrderAmount;
    private final Long maxDiscountAmount;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer usageLimit;
    private final Integer issueCount;
    private final Integer usedCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

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
