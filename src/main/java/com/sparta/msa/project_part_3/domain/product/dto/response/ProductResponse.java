package com.sparta.msa.project_part_3.domain.product.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

  UUID id;

  String name;

  String description;

  BigDecimal price;

  Integer stock;

  @JsonProperty("is_orderable")
  Boolean isOrderable;

  Long categoryId;
  String categoryName;

  public static ProductResponse of(Product product) {
      return ProductResponse.builder()
              .id(product.getId())
              .name(product.getName())
              .price(product.getPrice())
              .description(product.getDescription())
              .isOrderable(product.getStock() != null && product.getStock() > 0)
              .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
              .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
              .build();
  }

}
