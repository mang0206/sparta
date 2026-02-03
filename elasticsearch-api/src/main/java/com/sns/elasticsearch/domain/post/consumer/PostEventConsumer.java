package com.sns.elasticsearch.domain.post.consumer;

import com.sns.post.domain.post.event.PostCreatedEvent;
import com.sns.post.domain.post.event.PostDeletedEvent;
import com.sns.post.domain.post.event.PostUpdatedEvent;
import com.sns.elasticsearch.domain.post.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostEventConsumer {

    private final PostSearchService postSearchService;

    @KafkaListener(topics = "post.created", groupId = "elasticsearch-consumer-group")
    public void handlePostCreated(PostCreatedEvent event, Acknowledgment ack) {
        try {
            log.info("Consuming post created event: {}", event.getPostId());
            postSearchService.indexPost(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to index post: {}", event.getPostId(), e);
        }
    }

    @KafkaListener(topics = "post.updated", groupId = "elasticsearch-consumer-group")
    public void handlePostUpdated(PostUpdatedEvent event, Acknowledgment ack) {
        try {
            log.info("Consuming post updated event: {}", event.getPostId());
            postSearchService.updatePost(event);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to update post: {}", event.getPostId(), e);
        }
    }

    @KafkaListener(topics = "post.deleted", groupId = "elasticsearch-consumer-group")
    public void handlePostDeleted(PostDeletedEvent event, Acknowledgment ack) {
        try {
            log.info("Consuming post deleted event: {}", event.getPostId());
            postSearchService.deletePost(event.getPostId());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to delete post: {}", event.getPostId(), e);
        }
    }
}
