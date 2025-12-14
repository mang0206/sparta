package com.sparta.msa.project_part_3.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "coupon_user", indexes = {
    @Index(name = "idx_coupon_user_code", columnList = "code", unique = true),
    @Index(name = "idx_coupon_user_user_id", columnList = "user_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    Coupon coupon;

    @Column(name = "user_id")
    Long userId;

    @Column(nullable = false, unique = true)
    String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CouponUserStatus status;

    @Builder
    public CouponUser(Coupon coupon, String code) {
        this.coupon = coupon;
        this.code = code;
        this.status = CouponUserStatus.READY;
    }

    public void registerUser(Long userId) {
        this.userId = userId;
        this.status = CouponUserStatus.ISSUED;
    }

    public void useCoupon() {
        this.status = CouponUserStatus.USED;
    }
}
