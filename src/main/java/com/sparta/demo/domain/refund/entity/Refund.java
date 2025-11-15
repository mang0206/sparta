package com.sparta.demo.domain.refund.entity;

import com.sparta.demo.domain.Purcahse.entity.Purchase;
import com.sparta.demo.domain.User.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refund") // V1 스크립트 'refund' 테이블
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob // TEXT 타입
    @Column(name = "reason", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RefundStatus status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Refund(Purchase purchase, User user, String reason, RefundStatus status) {
        this.purchase = purchase;
        this.user = user;
        this.reason = reason;
        this.status = status;
    }

    //== 비즈니스 로직 (환불 처리) ==//
    public void process(RefundStatus newStatus) {
        if (this.status != RefundStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 환불 요청입니다.");
        }
        this.status = newStatus;
    }
}