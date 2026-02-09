package com.sparta.postapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventPublisher {

    private static final String POST_CREATED_TOPIC = "post.created";
    private static final String POST_UPDATED_TOPIC = "post.updated";
    private static final String POST_DELETED_TOPIC = "post.deleted";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreated(PostCreatedEvent event) {
        log.info("Publishing post created event: {}", event.getPostId());
        kafkaTemplate.send(POST_CREATED_TOPIC, event.getPostId().toString(), event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostUpdated(PostUpdatedEvent event) {
        log.info("Publishing post updated event: {}", event.getPostId());
        kafkaTemplate.send(POST_UPDATED_TOPIC, event.getPostId().toString(), event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostDeleted(PostDeletedEvent event) {
        log.info("Publishing post deleted event: {}", event.getPostId());
        kafkaTemplate.send(POST_DELETED_TOPIC, event.getPostId().toString(), event);
    }
}
