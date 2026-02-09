package com.sparta.postapi.event;

import com.sparta.postapi.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostUpdatedEvent {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;

    public static PostUpdatedEvent from(Post post) {
        return PostUpdatedEvent.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
