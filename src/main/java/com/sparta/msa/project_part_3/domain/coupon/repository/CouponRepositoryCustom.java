package com.sparta.msa.project_part_3.domain.coupon.repository;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponSearchCondition;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponRepositoryCustom {
    Page<Coupon> search(CouponSearchCondition condition, Pageable pageable);
    List<Coupon> findActiveCouponsForProduct(long productPrice);
}
