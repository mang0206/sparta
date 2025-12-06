package com.sparta.msa.project_part_3.domain.cart.service;

import com.sparta.msa.project_part_3.domain.cart.dto.AddCartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.dto.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cart.dto.UpdateCartItemRequest;
import com.sparta.msa.project_part_3.domain.cart.entity.CartItem;
import com.sparta.msa.project_part_3.domain.cart.repository.CartItemRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;

  @Transactional
  public void addItem(Long userId, AddCartItemRequest request) {
    Long productId = request.getProductId();
    Integer quantity = request.getQuantity();

    if (!productRepository.existsById(productId)) {
      throw new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT);
    }

    Optional<CartItem> optionalCartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

    if (optionalCartItem.isPresent()) {
      optionalCartItem.get().addQuantity(quantity);
    } else {
      cartItemRepository.save(CartItem.builder()
          .userId(userId)
          .productId(productId)
          .quantity(quantity)
          .build());
    }
  }

  public List<CartItemResponse> getCartList(Long userId) {
    List<CartItem> cartItems = cartItemRepository.findAllByUserId(userId);
    List<Long> productIds = cartItems.stream()
        .map(CartItem::getProductId)
        .collect(Collectors.toList());

    List<Product> products = productRepository.findAllById(productIds);
    Map<Long, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getId, Function.identity()));

    return cartItems.stream()
        .map(cartItem -> {
          Product product = productMap.get(cartItem.getProductId());
          String productName = product != null ? product.getName() : "Unknown Product";
          java.math.BigDecimal productPrice = product != null ? product.getPrice() : java.math.BigDecimal.ZERO;

          return CartItemResponse.builder()
              .id(cartItem.getId())
              .productId(cartItem.getProductId())
              .productName(productName)
              .productPrice(productPrice)
              .quantity(cartItem.getQuantity())
              .build();
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateQuantity(Long userId, Long productId, UpdateCartItemRequest request) {
    CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT)); // Reusing NOT_FOUND_PRODUCT for now as "Item in cart not found" isn't specified

    cartItem.updateQuantity(request.getQuantity());
  }

  @Transactional
  public void deleteItem(Long userId, Long productId) {
    // Check if item exists first to throw exception if needed, or just delete.
    // Requirement "Delete item from user's cart".
    if (!cartItemRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
        throw new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT);
    }
    cartItemRepository.deleteByUserIdAndProductId(userId, productId);
  }
}
