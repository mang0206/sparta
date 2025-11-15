package com.sparta.demo.domain.refund.service;

import com.sparta.demo.domain.Purcahse.entity.Purchase;
import com.sparta.demo.domain.Purcahse.entity.PurchaseItem;
import com.sparta.demo.domain.Purcahse.entity.PurchaseStatus;
import com.sparta.demo.domain.Purcahse.repository.PurchaseRepository;
import com.sparta.demo.domain.refund.dto.RefundProcessRequest;
import com.sparta.demo.domain.refund.dto.RefundRequest;
import com.sparta.demo.domain.refund.dto.RefundResponse;
import com.sparta.demo.domain.refund.entity.Refund;
import com.sparta.demo.domain.refund.entity.RefundStatus;
import com.sparta.demo.domain.refund.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundService {
    private final RefundRepository refundRepository;
    private final PurchaseRepository purchaseRepository;

    // 1. 환불 요청 API
    @Transactional
    public RefundResponse requestRefund(RefundRequest request) {
        Purchase purchase = purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매 ID입니다."));

        // (비즈니스 로직 1) 요청자와 구매자가 동일한지 확인
        if (!purchase.getUser().getId().equals(request.getUserId())) {
            throw new IllegalStateException("환불 요청 권한이 없습니다.");
        }

        // (비즈니스 로직 2) '완료'된 구매 건만 환불 요청 가능
        if (purchase.getStatus() != PurchaseStatus.COMPLETED) {
            throw new IllegalStateException("'COMPLETED' 상태의 구매 건만 환불 요청할 수 있습니다.");
        }


        Refund refund = Refund.builder()
                .purchase(purchase)
                .user(purchase.getUser())
                .reason(request.getReason())
                .status(RefundStatus.PENDING) // 초기 상태 PENDING
                .build();

        Refund savedRefund = refundRepository.save(refund);
        return new RefundResponse(savedRefund);
    }

    // 2. 환불 처리 API (관리자용)
    @Transactional
    public void processRefund(Long refundId, RefundStatus status) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환불 ID입니다."));

        if (status == RefundStatus.APPROVED) {
            refund.process(RefundStatus.APPROVED);
            // 재고 복원
            refund.getPurchase().getPurchaseItems().forEach(item ->
                    item.getProduct().increaseStock(item.getQuantity()));
        } else if (status == RefundStatus.REJECTED) {
            refund.process(RefundStatus.REJECTED);
        } else {
            throw new IllegalArgumentException("유효하지 않은 환불 처리 상태입니다.");
        }
    }

    // 3. 특정 사용자의 환불 목록 조회
    public List<RefundResponse> getRefundsByUser(Long userId) {
        return refundRepository.findAll().stream()
                .filter(refund -> refund.getUser().getId().equals(userId))
                .map(RefundResponse::new)
                .collect(Collectors.toList());
    }

    public List<RefundResponse> getAllRefunds() {
        return refundRepository.findAll().stream()
                .map(RefundResponse::new)
                .collect(Collectors.toList());
    }
}
