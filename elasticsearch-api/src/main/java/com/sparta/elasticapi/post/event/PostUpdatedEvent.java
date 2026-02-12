package com.sparta.elasticapi.post.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdatedEvent {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
}
