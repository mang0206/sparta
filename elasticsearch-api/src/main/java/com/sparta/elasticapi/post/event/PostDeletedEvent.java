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
public class PostDeletedEvent {
    private Long postId;
    private LocalDateTime deletedAt;
}
