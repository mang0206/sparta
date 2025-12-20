package com.sparta.msa.project_part_3.domain.product.entity;

import com.sparta.msa.project_part_3.domain.category.entity.Category;
import com.sparta.msa.project_part_3.global.entity.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Table
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

  @Id
  @Column(updatable = false, nullable = false, columnDefinition = "UUID")
  UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  Category category;

  String name;

  String description;

  BigDecimal price;

  Integer stock;

  Long externalProductId;

  @Builder
  public Product(
      Category category,
      String name,
      String description,
      BigDecimal price,
      Integer stock,
      Long externalProductId) {
    this.id = UUID.randomUUID();
    this.category = category;
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.externalProductId = externalProductId;
  }

  public void setCategory(Category category) {
    if (!ObjectUtils.isEmpty(category)) {
      this.category = category;
    }
  }

  public void setName(String name) {
    if (StringUtils.hasText(name)) {
      this.name = name;
    }
  }

  public void setDescription(String description) {
    if (StringUtils.hasText(description)) {
      this.description = description;
    }
  }

  public void setPrice(BigDecimal price) {
    if (!ObjectUtils.isEmpty(price)) {
      this.price = price;
    }
  }

  public void setStock(Integer stock) {
    if (!ObjectUtils.isEmpty(stock)) {
      this.stock = stock;
    }
  }

  public void setExternalProductId(Long externalProductId) {
      if (!ObjectUtils.isEmpty(externalProductId)) {
        this.externalProductId = externalProductId;
      }
  }
}
