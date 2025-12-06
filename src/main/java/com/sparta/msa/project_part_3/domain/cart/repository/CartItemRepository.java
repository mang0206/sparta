package com.sparta.msa.project_part_3.domain.cart.repository;

import com.sparta.msa.project_part_3.domain.cart.entity.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

  List<CartItem> findAllByUserId(Long userId);

  void deleteByUserIdAndProductId(Long userId, Long productId);
}
