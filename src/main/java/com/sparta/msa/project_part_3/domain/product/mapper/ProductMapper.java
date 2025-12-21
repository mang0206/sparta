package com.sparta.msa.project_part_3.domain.product.mapper;

import com.sparta.msa.project_part_3.domain.category.entity.Category;
import com.sparta.msa.project_part_3.domain.product.dto.request.ProductRequest;
import com.sparta.msa.project_part_3.domain.product.dto.response.ProductResponse;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(target = "categoryId", source = "category.id")
  @Mapping(target = "categoryName", source = "category.name")
  @Mapping(target = "isOrderable", expression = "java(product.getStock() != null && product.getStock() > 0)")
  ProductResponse toResponse(Product product);

  @Mapping(target = "category", source = "category")
  @Mapping(target = "name", source = "request.name")
  @Mapping(target = "externalProductId", ignore = true)
  Product toEntity(ProductRequest request, Category category);

  default Page<ProductResponse> toResponsePage(Page<Product> products) {
    return products.map(this::toResponse);
  }

}
