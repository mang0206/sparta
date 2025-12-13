package com.sparta.msa.project_part_3.domain.coupon.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, precision = 19, scale = 2)
    BigDecimal discount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CouponStatus status;

    @Column(nullable = false)
    LocalDateTime validFrom;

    @Column(nullable = false)
    LocalDateTime validTo;

    public Coupon(String name, BigDecimal discount, CouponStatus status, LocalDateTime validFrom, LocalDateTime validTo) {
        this.name = name;
        this.discount = discount;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
}
