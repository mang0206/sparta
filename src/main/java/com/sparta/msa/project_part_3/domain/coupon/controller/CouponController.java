package com.sparta.msa.project_part_3.domain.coupon.controller;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.OfflineCouponClaimRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.OfflineCouponGenerateRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/definitions")
    public ResponseEntity<Long> createCouponDefinition(@RequestBody com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(couponService.createCoupon(request));
    }

    @PostMapping("/generate")
    public ResponseEntity<Void> generateOfflineCoupons(@RequestBody OfflineCouponGenerateRequest request) {
        couponService.generateOfflineCoupons(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/issuance")
    public ResponseEntity<Void> claimOfflineCoupon(@RequestBody OfflineCouponClaimRequest request) {
        couponService.claimOfflineCoupon(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CouponResponse>> getUserCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(couponService.getUserCoupons(userId));
    }
}
