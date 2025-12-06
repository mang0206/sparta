package com.sparta.msa.project_part_3.domain.cart.service;

import com.sparta.msa.project_part_3.domain.cart.dto.request.CartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.dto.response.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cart.entity.CartItem;

import com.sparta.msa.project_part_3.domain.cart.repository.CartItemRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addItem(Long userId, CartItemRequest request) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElseGet(() -> createCartItem(userId, request));

        if (cartItem.getId() != null) {
            cartItem.increaseQuantity(request.getQuantity());
        }

        cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getItems(Long userId) {
        return cartItemRepository.findByProductByUserId(userId)
                .stream()
                .map(cartItem -> CartItemResponse.builder()
                        .productId(cartItem.getProduct().getId())
                        .productName(cartItem.getProduct().getName())
                        .description(cartItem.getProduct().getDescription())
                        .price(cartItem.getProduct().getPrice())
                        .quantity(cartItem.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateQuantity(Long userId, CartItemRequest request) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.CART_ITEM_NOT_FOUND));

        cartItem.changeQuantity(request.getQuantity());
    }

    @Transactional
    public void deleteItem(Long userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.CART_ITEM_NOT_FOUND));

        cartItemRepository.delete(cartItem);
    }

    private CartItem createCartItem(Long userId, CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT));

        return CartItem.builder()
                .userId(userId)
                .product(product)
                .quantity(request.getQuantity())
                .build();
    }
}