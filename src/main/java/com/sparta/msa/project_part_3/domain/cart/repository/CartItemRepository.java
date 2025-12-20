package com.sparta.msa.project_part_3.domain.cart.repository;

import com.sparta.msa.project_part_3.domain.cart.entity.CartItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUserIdAndProductId(Long userId, UUID productId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.userId = :userId")
    List<CartItem> findByProductByUserId(@Param("userId") Long userId);
}