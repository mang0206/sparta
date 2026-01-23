package com.ecommerce.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    @PostConstruct
    public void init() {
        System.out.println("OrderEventConsumer initialized and ready to consume messages");
    }

    @KafkaListener(
            topics = "order-event",
            groupId = "order-api"
    )
    public void consumeOrderEvent(String orderId) {
        System.out.println("=== Order Event Consumed ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Order event processed successfully: " + orderId);
    }
}
