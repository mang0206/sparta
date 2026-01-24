package com.ecommerce.paymentapi.kafka;

import com.ecommerce.paymentapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order-event", groupId = "order-api")
    public void consume(String orderId, Acknowledgment ack) {
        log.info("Received Order Event (OrderId): {}", orderId);
        paymentService.processPayment(Long.valueOf(orderId));
        ack.acknowledge();
    }
}
