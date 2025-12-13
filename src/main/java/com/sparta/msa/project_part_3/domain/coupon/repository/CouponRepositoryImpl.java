package com.sparta.msa.project_part_3.domain.coupon.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponSearchCondition;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.QCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.msa.project_part_3.domain.coupon.entity.QCoupon.coupon;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Coupon> search(CouponSearchCondition condition, Pageable pageable) {
        List<Coupon> content = queryFactory
                .selectFrom(coupon)
                .where(
                        isActive(condition.getIsActive()),
                        coupon.isDeleted.isFalse() // 기본적으로 삭제된 쿠폰은 제외 (혹은 요구사항에 따라 관리자 기능이면 포함할 수도 있음. 여기선 목록조회라 제외가 맞을듯)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(coupon.id.desc())
                .fetch();

        Long count = queryFactory
                .select(coupon.count())
                .from(coupon)
                .where(
                        isActive(condition.getIsActive()),
                        coupon.isDeleted.isFalse()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count != null ? count : 0);
    }

    @Override
    public List<Coupon> findActiveCouponsForProduct(long productPrice) {
        LocalDateTime now = LocalDateTime.now();

        return queryFactory
                .selectFrom(coupon)
                .where(
                        coupon.isDeleted.isFalse(),
                        coupon.startDate.loe(now),
                        coupon.endDate.goe(now),
                        coupon.minOrderAmount.loe(productPrice),
                        // usageLimit == 0 OR usageLimit > usedCount
                        coupon.usageLimit.eq(0).or(coupon.usageLimit.gt(coupon.usedCount))
                )
                .fetch();
    }

    private BooleanExpression isActive(Boolean isActive) {
        if (isActive == null) {
            return null;
        }
        if (isActive) {
            LocalDateTime now = LocalDateTime.now();
            return coupon.startDate.loe(now)
                    .and(coupon.endDate.goe(now));
        } else {
             LocalDateTime now = LocalDateTime.now();
            // 비활성화 = 기간 전 or 기간 후
            return coupon.startDate.gt(now)
                    .or(coupon.endDate.lt(now));
        }
    }
}
