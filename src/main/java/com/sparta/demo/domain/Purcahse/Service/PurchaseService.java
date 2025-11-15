package com.sparta.demo.domain.Purcahse.Service;

import com.sparta.demo.domain.Purcahse.dto.PurchaseItemRequest;
import com.sparta.demo.domain.Purcahse.dto.PurchaseRequest;
import com.sparta.demo.domain.Purcahse.dto.PurchaseResponse;
import com.sparta.demo.domain.Purcahse.entity.Purchase;
import com.sparta.demo.domain.Purcahse.entity.PurchaseItem;
import com.sparta.demo.domain.Purcahse.entity.PurchaseStatus;
import com.sparta.demo.domain.Purcahse.repository.PurchaseRepository;
import com.sparta.demo.domain.User.entity.User;
import com.sparta.demo.domain.User.repository.UserRepository;
import com.sparta.demo.domain.product.entity.Product;
import com.sparta.demo.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // 1. 구매 생성
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        // 회원 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID입니다."));

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<PurchaseItem> purchaseItems = new ArrayList<>();

        // 주문 상품(OrderItem) 생성 및 재고 차감
        for (PurchaseItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID입니다."));

            //재고 차감
            product.decreaseStock(itemRequest.getQuantity());

            // 주문 항목 생성
            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .build();

            purchaseItems.add(purchaseItem);
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        // 주문 생성
        Purchase purchase = Purchase.builder()
                .user(user)
                .status(PurchaseStatus.PENDING)
                .totalPrice(totalPrice)
                .build();

        for (PurchaseItem purchaseItem : purchaseItems) {
            purchase.addPurchaseItem(purchaseItem);
        }

        //Purchase 저장 시 PurchaseItem도 함께 저장됨 (CascadeType.ALL)
        Purchase savedPurchase = purchaseRepository.save(purchase);
        return new PurchaseResponse(savedPurchase);
    }

    // 2. 특정 사용자의 구매 목록 조회
    public List<PurchaseResponse> getPurchasesByUserId(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByUserId(userId);
        return purchases.stream()
                .map(PurchaseResponse::new)
                .collect(Collectors.toList());
    }

    // 3. 구매 상태 변경 (완료)
    @Transactional
    public PurchaseResponse completePurchase(Long purchaseId) {
        Purchase purchase = findPurchaseById(purchaseId);

        if (purchase.getStatus() != PurchaseStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태의 구매만 완료 처리할 수 있습니다.");
        }

        purchase.changeStatus(PurchaseStatus.COMPLETED);
        return new PurchaseResponse(purchase);
    }

    // 4. 구매 취소 (재고 복원)
    @Transactional
    public PurchaseResponse cancelPurchase(Long purchaseId) {
        Purchase purchase = findPurchaseById(purchaseId);

        if (purchase.getStatus() != PurchaseStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태의 구매만 취소할 수 있습니다.");
        }

        for (PurchaseItem item : purchase.getPurchaseItems()) {
            item.getProduct().increaseStock(item.getQuantity());
        }

        purchase.changeStatus(PurchaseStatus.CANCELED);
        return new PurchaseResponse(purchase);
    }

    private Purchase findPurchaseById(Long purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매 ID입니다."));
    }
}
