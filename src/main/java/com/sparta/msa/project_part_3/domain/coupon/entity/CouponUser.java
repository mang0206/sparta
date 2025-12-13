package com.sparta.msa.project_part_3.domain.coupon.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "coupon_user")
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

    public CouponUser(Coupon coupon, String code, CouponUserStatus status) {
        this.coupon = coupon;
        this.code = code;
        this.status = status;
    }

    public void claim(Long userId) {
        this.userId = userId;
        this.status = CouponUserStatus.ISSUED;
    }
}
