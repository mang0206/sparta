package com.ecommerce.order.event;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String topicName = "order-event";

  public void publishOrderCreated(Long orderId) {
    log.info("Kafka 메시지 전송 시도: {}", orderId);
    try {
      kafkaTemplate.send(topicName, orderId.toString())
          .addCallback(
              result -> System.out.println(
                  "Message sent successfully to topic: " + topicName + ", partition: " +
                      Objects.requireNonNull(result).getRecordMetadata().partition()),
              ex -> System.out.println("Failed to send message to topic: {}" + topicName)
          );
    } catch (Exception e) {
      log.error("Error publishing order created event", e);
      throw new RuntimeException("Failed to publish order event", e);
    }
  }
}
