package com.ecommerce.paymentapi.service;

import com.ecommerce.paymentapi.entity.Payment;
import com.ecommerce.paymentapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public void processPayment(Long orderId) {
        log.info("Processing payment for orderId: {}", orderId);

        Payment payment = Payment.builder()
                .orderId(orderId)
                .status("COMPLETED")
                .build();
        paymentRepository.save(payment);
        log.info("Payment processed and saved for orderId: {}", orderId);
    }
}