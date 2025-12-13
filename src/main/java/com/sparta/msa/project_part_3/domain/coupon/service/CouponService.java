package com.sparta.msa.project_part_3.domain.coupon.service;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.OfflineCouponClaimRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.OfflineCouponGenerateRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUserStatus;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponRepository;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponUserRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;

    @Transactional
    public Long createCoupon(com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponCreateRequest request) {
        Coupon coupon = new Coupon(request.getName(), request.getDiscount(), com.sparta.msa.project_part_3.domain.coupon.entity.CouponStatus.ACTIVE, request.getValidFrom(), request.getValidTo());
        return couponRepository.save(coupon).getId();
    }

    @Transactional
    public void generateOfflineCoupons(OfflineCouponGenerateRequest request) {
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        List<CouponUser> couponUsers = new ArrayList<>();
        for (int i = 0; i < request.getCount(); i++) {
            String code = UUID.randomUUID().toString();
            couponUsers.add(new CouponUser(coupon, code, CouponUserStatus.READY));
        }
        couponUserRepository.saveAll(couponUsers);
    }

    @Transactional
    public void claimOfflineCoupon(OfflineCouponClaimRequest request) {
        CouponUser couponUser = couponUserRepository.findByCodeWithLock(request.getCouponCode())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.INVALID_COUPON_CODE));

        if (couponUser.getUserId() != null || couponUser.getStatus() != CouponUserStatus.READY) {
             throw new DomainException(DomainExceptionCode.ALREADY_USED_COUPON);
        }

        couponUser.claim(request.getUserId());
        couponUserRepository.save(couponUser);
    }

    public List<CouponResponse> getUserCoupons(Long userId) {
        return couponUserRepository.findByUserId(userId).stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }
}
