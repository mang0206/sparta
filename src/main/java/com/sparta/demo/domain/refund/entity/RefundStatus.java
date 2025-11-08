package com.sparta.demo.domain.refund.entity;

public enum RefundStatus {
    PENDING,  // 환불 요청 (대기)
    APPROVED, // 환불 승인
    REJECTED  // 환불 거절
}