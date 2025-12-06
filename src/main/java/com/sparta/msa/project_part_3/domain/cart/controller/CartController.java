package com.sparta.msa.project_part_3.domain.cart.controller;

import com.sparta.msa.project_part_3.domain.cart.dto.request.CartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.dto.request.UpdateCartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.dto.response.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cart.service.CartService;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ApiResponse<Void> addItem(@Valid @RequestBody CartItemRequest request,
                                     Authentication authentication) {
        Long userId = extractUserId(authentication);
        cartService.addItem(userId, request);
        return ApiResponse.ok();
    }

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getItems(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ApiResponse.ok(cartService.getItems(userId));
    }

    @PutMapping("/{productId}")
    public ApiResponse<Void> updateQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {

        Long userId = extractUserId(authentication);

        cartService.updateQuantity(userId, productId, request.getQuantity());

        return ApiResponse.ok();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteItem(@PathVariable Long productId,
                                        Authentication authentication) {
        Long userId = extractUserId(authentication);
        cartService.deleteItem(userId, productId);
        return ApiResponse.ok();
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new DomainException(DomainExceptionCode.NOT_FOUND_USER);
        }

        try {
            Object principal = authentication.getPrincipal();
            return (Long) principal.getClass().getMethod("getUserId").invoke(principal);
        } catch (Exception e) {
            throw new DomainException(DomainExceptionCode.NOT_FOUND_USER);
        }
    }
}