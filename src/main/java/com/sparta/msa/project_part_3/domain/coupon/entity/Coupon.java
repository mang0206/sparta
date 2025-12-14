package com.sparta.msa.project_part_3.domain.coupon.entity;

import com.sparta.msa.project_part_3.global.entity.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "coupon")
@Getter
@DynamicInsert
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String couponName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private BigDecimal minOrderAmount;

    @Column
    private BigDecimal maxDiscountAmount;

    @Column(nullable = false)
    LocalDateTime startDate;

    @Column(nullable = false)
    LocalDateTime endDate;

    @Column
    Integer usageLimit;

    @Column(nullable = false)
    Integer issueCount;

    @Column(nullable = false)
    Integer usedCount;

    @Column(nullable = false)
    Boolean isDeleted;

    @Builder
    public Coupon(String couponName,
                  DiscountType discountType,
                  BigDecimal discountValue,
                  BigDecimal minOrderAmount,
                  BigDecimal maxDiscountAmount,
                  LocalDateTime startDate,
                  LocalDateTime endDate,
                  Integer usageLimit,
                  Integer issueCount) {
        this.couponName = couponName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.issueCount = issueCount == null ? 0 : issueCount;
        this.usedCount = 0;
        this.isDeleted = false;
    }

    public boolean isValid(LocalDateTime now) {
        return !isDeleted &&
                (now.isEqual(startDate) || now.isAfter(startDate)) &&
                (now.isEqual(endDate) || now.isBefore(endDate)) &&
                (usageLimit == 0 || usedCount < usageLimit);
    }

    public void delete() {
        this.isDeleted = true;
    }
}