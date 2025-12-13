package com.sparta.msa.project_part_3.domain.coupon.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCreateRequest {

  @NotBlank(message = "쿠폰 이름은 필수입니다.")
  String name;

  @NotNull(message = "할인 금액은 필수입니다.")
  @Min(value = 0, message = "할인 금액은 0 이상이어야 합니다.")
  BigDecimal discount;

  @Min(value = 0, message = "발행 개수는 0 이상이어야 합니다.")
  int count;
}
