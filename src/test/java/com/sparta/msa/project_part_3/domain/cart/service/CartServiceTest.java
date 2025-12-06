package com.sparta.msa.project_part_3.domain.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.sparta.msa.project_part_3.domain.cart.dto.AddCartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.dto.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cart.dto.UpdateCartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.entity.CartItem;
import com.sparta.msa.project_part_3.domain.cart.repository.CartItemRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

  @Mock
  private CartItemRepository cartItemRepository;

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private CartService cartService;

  @Test
  void addItem_NewItem() {
    // Given
    Long userId = 1L;
    AddCartItemRequest request = new AddCartItemRequest();
    ReflectionTestUtils.setField(request, "productId", 10L);
    ReflectionTestUtils.setField(request, "quantity", 2);

    given(productRepository.existsById(10L)).willReturn(true);
    given(cartItemRepository.findByUserIdAndProductId(userId, 10L)).willReturn(Optional.empty());

    // When
    cartService.addItem(userId, request);

    // Then
    verify(cartItemRepository).save(any(CartItem.class));
  }

  @Test
  void addItem_ExistingItem() {
    // Given
    Long userId = 1L;
    AddCartItemRequest request = new AddCartItemRequest();
    ReflectionTestUtils.setField(request, "productId", 10L);
    ReflectionTestUtils.setField(request, "quantity", 2);

    CartItem existingItem = CartItem.builder()
        .userId(userId)
        .productId(10L)
        .quantity(1)
        .build();

    given(productRepository.existsById(10L)).willReturn(true);
    given(cartItemRepository.findByUserIdAndProductId(userId, 10L)).willReturn(Optional.of(existingItem));

    // When
    cartService.addItem(userId, request);

    // Then
    assertEquals(3, existingItem.getQuantity());
  }

  @Test
  void getCartList() {
    // Given
    Long userId = 1L;
    CartItem cartItem = CartItem.builder()
        .userId(userId)
        .productId(10L)
        .quantity(2)
        .build();

    Product product = Product.builder()
        .name("Test Product")
        .price(BigDecimal.valueOf(1000))
        .build();
    ReflectionTestUtils.setField(product, "id", 10L);

    given(cartItemRepository.findAllByUserId(userId)).willReturn(List.of(cartItem));
    given(productRepository.findAllById(anyList())).willReturn(List.of(product));

    // When
    List<CartItemResponse> result = cartService.getCartList(userId);

    // Then
    assertEquals(1, result.size());
    assertEquals("Test Product", result.get(0).getProductName());
    assertEquals(BigDecimal.valueOf(1000), result.get(0).getProductPrice());
  }

  @Test
  void deleteItem() {
      Long userId = 1L;
      Long productId = 10L;

      given(cartItemRepository.findByUserIdAndProductId(userId, productId)).willReturn(Optional.of(CartItem.builder().build()));

      cartService.deleteItem(userId, productId);

      verify(cartItemRepository, times(1)).deleteByUserIdAndProductId(userId, productId);
  }
}
