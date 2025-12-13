package com.sparta.msa.project_part_3.domain.coupon.service;

import com.sparta.msa.project_part_3.domain.coupon.dto.CouponRequestDto;
import com.sparta.msa.project_part_3.domain.coupon.dto.CouponResponseDto;
import com.sparta.msa.project_part_3.domain.coupon.dto.CouponSearchCondition;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final ProductRepository productRepository; // For product price lookup

    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto requestDto) {
        if (requestDto.getDiscountType() == DiscountType.PERCENTAGE) {
            if (requestDto.getDiscountValue() > 100) {
                 throw new IllegalArgumentException("할인율은 100%를 초과할 수 없습니다.");
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

        return new CouponResponseDto(couponRepository.save(coupon));
    }

    public CouponResponseDto getCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        return new CouponResponseDto(coupon);
    }

    public Page<CouponResponseDto> getCoupons(CouponSearchCondition condition, Pageable pageable) {
        return couponRepository.search(condition, pageable)
                .map(CouponResponseDto::new);
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        coupon.delete();
    }

    // 상품에 적용 가능한 최대 할인율(금액)을 가진 쿠폰 찾기
    public CouponResponseDto getMaxDiscountForProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

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
                currentDiscountAmount = coupon.getDiscountValue();
            } else {
                currentDiscountAmount = (productPrice * coupon.getDiscountValue()) / 100;
                if (coupon.getMaxDiscountAmount() != null && currentDiscountAmount > coupon.getMaxDiscountAmount()) {
                    currentDiscountAmount = coupon.getMaxDiscountAmount();
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

        return new CouponResponseDto(bestCoupon);
    }
}
