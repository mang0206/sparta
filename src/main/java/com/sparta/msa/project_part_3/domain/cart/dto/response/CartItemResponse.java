package com.sparta.msa.project_part_3.domain.cart.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private UUID productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer quantity;
}