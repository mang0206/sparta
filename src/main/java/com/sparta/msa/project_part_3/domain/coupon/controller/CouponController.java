package com.sparta.msa.project_part_3.domain.coupon.controller;

import com.sparta.msa.project_part_3.domain.coupon.dto.CouponRequestDto;
import com.sparta.msa.project_part_3.domain.coupon.dto.CouponResponseDto;
import com.sparta.msa.project_part_3.domain.coupon.dto.CouponSearchCondition;
import com.sparta.msa.project_part_3.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponseDto> createCoupon(@Valid @RequestBody CouponRequestDto requestDto) {
        return ResponseEntity.ok(couponService.createCoupon(requestDto));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponseDto> getCoupon(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.getCoupon(couponId));
    }

    @GetMapping
    public ResponseEntity<Page<CouponResponseDto>> getCoupons(
            @ModelAttribute CouponSearchCondition condition,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(couponService.getCoupons(condition, pageable));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}
