package com.sparta.demo.domain.Purcahse.repository;

import com.sparta.demo.domain.Purcahse.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> { // Order -> Purchase

    // 특정 사용자의 구매 목록 조회
    List<Purchase> findByUserId(Long userId);
}