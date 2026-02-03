package com.sns.elasticsearch.domain.post.dto;

import com.sns.elasticsearch.domain.post.document.PostDocument;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostSearchResponse {
    private String id;
    private String title;
    private String content;
    private Long authorId;
    private LocalDateTime createdAt;

    public static PostSearchResponse from(PostDocument document) {
        return PostSearchResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .authorId(document.getAuthorId())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
