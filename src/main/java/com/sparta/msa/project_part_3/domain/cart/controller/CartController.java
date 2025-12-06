package com.sparta.msa.project_part_3.domain.cart.controller;

import com.sparta.msa.project_part_3.domain.cart.dto.AddCartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.dto.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cart.dto.UpdateCartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.service.CartService;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import com.sparta.msa.project_part_3.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping
  public ApiResponse<Void> addItem(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody AddCartItemRequest request) {
    cartService.addItem(userDetails.getUserId(), request);
    return ApiResponse.<Void>builder()
        .message("Item added to cart")
        .build();
  }

  @GetMapping
  public ApiResponse<List<CartItemResponse>> getCartList(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    List<CartItemResponse> responses = cartService.getCartList(userDetails.getUserId());
    return ApiResponse.<List<CartItemResponse>>builder()
        .data(responses)
        .build();
  }

  @PatchMapping("/{productId}")
  public ApiResponse<Void> updateQuantity(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long productId,
      @Valid @RequestBody UpdateCartItemRequest request) {
    cartService.updateQuantity(userDetails.getUserId(), productId, request);
    return ApiResponse.<Void>builder()
        .message("Cart item updated")
        .build();
  }

  @DeleteMapping("/{productId}")
  public ApiResponse<Void> deleteItem(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long productId) {
    cartService.deleteItem(userDetails.getUserId(), productId);
    return ApiResponse.<Void>builder()
        .message("Cart item deleted")
        .build();
  }
}
