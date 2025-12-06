package com.sparta.msa.project_part_3.domain.cart.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {

  private Long id;
  private Long productId;
  private String productName;
  private BigDecimal productPrice;
  private Integer quantity;
}
