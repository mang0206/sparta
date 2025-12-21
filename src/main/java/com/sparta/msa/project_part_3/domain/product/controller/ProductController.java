package com.sparta.msa.project_part_3.domain.product.controller;

import com.sparta.msa.project_part_3.domain.product.dto.request.ProductRequest;
import com.sparta.msa.project_part_3.domain.product.dto.request.ProductSearchCondition;
import com.sparta.msa.project_part_3.domain.product.dto.response.ProductResponse;
import com.sparta.msa.project_part_3.domain.product.service.ProductService;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import com.sparta.msa.project_part_3.global.response.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  /*public ApiResponse<PageResult<ProductResponse>> findProductByPageable(Pageable pageable) {
    return ApiResponse.ok(productService.findProductByPageable(pageable));
  }*/
  public ApiResponse<PageResult<ProductResponse>> findProductByPageable(
          @RequestParam(required = false) String category,
          @RequestParam(required = false) Boolean is_orderable,
          Pageable pageable) {

      ProductSearchCondition condition = ProductSearchCondition.builder()
              .categoryId(category)
              .isOrderable(is_orderable)
              .build();
      return ApiResponse.ok(productService.searchProducts(condition, pageable));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<Void> create(@Valid @RequestBody ProductRequest request) {
    productService.create(request);
    return ApiResponse.ok();
  }

  @PutMapping("/{productId}")
  public ApiResponse<Void> update(@PathVariable UUID productId,
      @Valid @RequestBody ProductRequest request) {
    productService.update(productId, request);
    return ApiResponse.ok();
  }

  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ApiResponse<Void> delete(@PathVariable UUID productId) {
    productService.delete(productId);
    return ApiResponse.ok();
  }

}
