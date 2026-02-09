package com.sparta.postapi.event;

import com.sparta.postapi.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDeletedEvent {
    private Long postId;
    private LocalDateTime deletedAt;

    public static PostDeletedEvent from(Post post) {
        return PostDeletedEvent.builder()
                .postId(post.getId())
                .deletedAt(post.getDeletedAt())
                .build();
    }
}
