package com.sparta.msa.project_part_3.domain.coupon.service;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRegistrationRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponSearchCondition;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.OfflineCouponRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponUserResponse;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUserStatus;
import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponRepository;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponUserRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final ProductRepository productRepository; // For product price lookup
    private final CouponUserRepository couponUserRepository;

    @Transactional
    public List<CouponUserResponse> createOfflineCoupons(OfflineCouponRequest requestDto) {
        Coupon coupon = couponRepository.findById(requestDto.getCouponId())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        if (Boolean.TRUE.equals(coupon.getIsDeleted())) {
            throw new DomainException(DomainExceptionCode.NOT_FOUND_COUPON);
        }

        List<CouponUser> couponUsers = new ArrayList<>();
        for (int i = 0; i < requestDto.getCount(); i++) {
            String code = UUID.randomUUID().toString();
            // Ensure uniqueness if needed, though UUID collision is rare.
            // In high volume, retry logic might be needed, but for now simple UUID.
            CouponUser couponUser = CouponUser.builder()
                    .coupon(coupon)
                    .code(code)
                    .build();
            couponUsers.add(couponUser);
        }

        List<CouponUser> saved = couponUserRepository.saveAll(couponUsers);
        return saved.stream().map(CouponUserResponse::from).toList();
    }

    @Transactional
    public CouponUserResponse registerOfflineCoupon(CouponRegistrationRequest requestDto) {
        // Pessimistic Lock for Double Claiming Prevention
        CouponUser couponUser = couponUserRepository.findByCodeForUpdate(requestDto.getCouponCode())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON)); // OR INVALID_COUPON_CODE

        if (couponUser.getStatus() != CouponUserStatus.READY) {
            // Already used or issued
            throw new DomainException(DomainExceptionCode.ALREADY_USED_COUPON); // Need to add this code if not exists
        }

        // Also check if the underlying coupon is still valid?
        // Usually offline coupons are valid if the code is valid, but let's check basic validity
        if (Boolean.TRUE.equals(couponUser.getCoupon().getIsDeleted())) {
             throw new DomainException(DomainExceptionCode.NOT_FOUND_COUPON);
        }

        couponUser.registerUser(requestDto.getUserId());
        return CouponUserResponse.from(couponUser);
    }

    @Transactional(readOnly = true)
    public List<CouponUserResponse> getUserCoupons(Long userId) {
        return couponUserRepository.findByUserId(userId).stream()
                .map(CouponUserResponse::from)
                .toList();
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest requestDto) {
        if (requestDto.getDiscountType() == DiscountType.PERCENTAGE) {
            if (requestDto.getDiscountValue().intValue() > 100) {
                throw new DomainException(DomainExceptionCode.INVALID_COUPON_DISCOUNT_RATE);
            }
        }

        Coupon coupon = Coupon.builder()
                .couponName(requestDto.getCouponName())
                .discountType(requestDto.getDiscountType())
                .discountValue(requestDto.getDiscountValue())
                .minOrderAmount(requestDto.getMinOrderAmount())
                .maxDiscountAmount(requestDto.getMaxDiscountAmount())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .usageLimit(requestDto.getUsageLimit())
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);
        return CouponResponse.from(savedCoupon);
    }

    public CouponResponse getCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        if (Boolean.TRUE.equals(coupon.getIsDeleted())) {
            throw new DomainException(DomainExceptionCode.NOT_FOUND_COUPON);
        }

        return CouponResponse.from(coupon);
    }

    public Page<CouponResponse> getCoupons(CouponSearchCondition condition, Pageable pageable) {
        return couponRepository.search(condition, pageable)
                .map(CouponResponse::from);
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));
        coupon.delete();
    }

    // 상품에 적용 가능한 최대 할인율(금액)을 가진 쿠폰 찾기
    public CouponResponse getMaxDiscountForProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT));

        long productPrice = product.getPrice().longValue();

        List<Coupon> activeCoupons = couponRepository.findActiveCouponsForProduct(productPrice);

        if (activeCoupons.isEmpty()) {
            return null;
        }

        Coupon bestCoupon = null;
        long maxDiscountAmount = -1;

        for (Coupon coupon : activeCoupons) {
            long currentDiscountAmount = 0;
            if (coupon.getDiscountType() == DiscountType.FIXED) {
                currentDiscountAmount = coupon.getDiscountValue().longValue();
            } else {
                currentDiscountAmount = (productPrice * coupon.getDiscountValue().longValue()) / 100;
                if (coupon.getMaxDiscountAmount() != null && currentDiscountAmount > coupon.getMaxDiscountAmount().longValue()) {
                    currentDiscountAmount = coupon.getMaxDiscountAmount().longValue();
                }
            }

            if (currentDiscountAmount > productPrice) {
                currentDiscountAmount = productPrice;
            }

            if (currentDiscountAmount > maxDiscountAmount) {
                maxDiscountAmount = currentDiscountAmount;
                bestCoupon = coupon;
            } else if (currentDiscountAmount == maxDiscountAmount) {
                if (bestCoupon != null && coupon.getDiscountType() == DiscountType.PERCENTAGE && bestCoupon.getDiscountType() == DiscountType.FIXED) {
                    bestCoupon = coupon;
                } else if (bestCoupon == null) {
                    bestCoupon = coupon;
                }
            }
        }

        if (bestCoupon == null) return null;

        return CouponResponse.from(bestCoupon);
    }
}