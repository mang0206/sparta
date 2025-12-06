package com.sparta.msa.project_part_3.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddCartItemRequest {

  @NotNull
  private Long productId;

  @NotNull
  @Min(1)
  private Integer quantity;
}
