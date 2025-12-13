package com.sparta.msa.project_part_3.domain.coupon.service;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponSearchCondition;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final ProductRepository productRepository; // For product price lookup

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
                // If amounts are equal, prefer PERCENTAGE as per requirement "percentage applied first"
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
