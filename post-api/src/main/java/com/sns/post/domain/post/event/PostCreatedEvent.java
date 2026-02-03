package com.sns.post.domain.post.event;

import com.sns.post.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostCreatedEvent {
    private Long postId;
    private String title;
    private String content;
    private Long authorId;
    private LocalDateTime createdAt;

    public static PostCreatedEvent from(Post post) {
        return PostCreatedEvent.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthorId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
