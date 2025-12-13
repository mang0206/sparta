package com.sparta.msa.project_part_3.domain.coupon.service;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRequestDto;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponseDto;
import com.sparta.msa.project_part_3.domain.coupon.entity.DiscountType;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    @DisplayName("쿠폰 생성 테스트")
    void createCoupon() {
        // given
        CouponRequestDto requestDto = new CouponRequestDto();
        ReflectionTestUtils.setField(requestDto, "couponName", "테스트 쿠폰");
        ReflectionTestUtils.setField(requestDto, "discountType", DiscountType.FIXED);
        ReflectionTestUtils.setField(requestDto, "discountValue", 1000L);
        ReflectionTestUtils.setField(requestDto, "minOrderAmount", 5000L);
        ReflectionTestUtils.setField(requestDto, "startDate", LocalDateTime.now());
        ReflectionTestUtils.setField(requestDto, "endDate", LocalDateTime.now().plusDays(1));
        ReflectionTestUtils.setField(requestDto, "usageLimit", 100);

        given(couponRepository.save(any())).willAnswer(invocation -> {
            var coupon = invocation.getArgument(0);
            ReflectionTestUtils.setField(coupon, "id", 1L);
            return coupon;
        });

        // when
        CouponResponseDto response = couponService.createCoupon(requestDto);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getCouponName()).isEqualTo("테스트 쿠폰");
        assertThat(response.getDiscountType()).isEqualTo(DiscountType.FIXED);
    }
}
