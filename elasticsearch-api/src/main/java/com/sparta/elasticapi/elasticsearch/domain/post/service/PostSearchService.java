package com.sparta.elasticapi.elasticsearch.domain.post.service;

import com.sparta.elasticapi.elasticsearch.domain.post.document.PostDocument;
import com.sparta.elasticapi.elasticsearch.domain.post.dto.PostSearchResponse;
import com.sparta.elasticapi.elasticsearch.domain.post.repository.PostSearchRepository;
import com.sparta.elasticapi.elasticsearch.elasticsearch.PostIndexManager;
import com.sparta.elasticapi.post.event.PostCreatedEvent;
import com.sparta.elasticapi.post.event.PostUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final PostSearchRepository postSearchRepository;
    private final PostIndexManager postIndexManager;

    public void indexPost(PostCreatedEvent event) {
        PostDocument document = PostDocument.builder()
                .id(event.getPostId().toString())
                .title(event.getTitle())
                .content(event.getContent())
                .authorId(event.getAuthorId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getCreatedAt()) // Initially same
                .build();

        postIndexManager.index(document);
    }

    public void updatePost(PostUpdatedEvent event) {
        postSearchRepository.findById(event.getPostId().toString())
                .ifPresent(document -> {
                    PostDocument updated = PostDocument.builder()
                            .id(document.getId())
                            .title(event.getTitle())
                            .content(event.getContent())
                            .authorId(document.getAuthorId())
                            .createdAt(document.getCreatedAt())
                            .updatedAt(event.getUpdatedAt())
                            .build();
                    postIndexManager.index(updated);
                });
    }

    public void deletePost(Long postId) {
        postIndexManager.delete(postId.toString());
    }

    public List<PostSearchResponse> searchPosts(String keyword) {
        return postSearchRepository.findByTitleContainingOrContentContaining(keyword, keyword)
                .stream()
                .map(PostSearchResponse::from)
                .collect(Collectors.toList());
    }
}
