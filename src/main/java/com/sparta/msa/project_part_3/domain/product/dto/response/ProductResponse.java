package com.sparta.msa.project_part_3.domain.product.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

  UUID id;

  ProductCategoryResponse category;

  String name;

  String description;

  BigDecimal price;

  Integer stock;

  @Getter
  @Builder
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class ProductCategoryResponse {

    String id;

    String name;

  }

}
