package com.sparta.postapi.controller;

import com.sparta.postapi.dto.PostCreateRequest;
import com.sparta.postapi.dto.PostResponse;
import com.sparta.postapi.dto.PostUpdateRequest;
import com.sparta.postapi.entity.Post;
import com.sparta.postapi.event.PostCreatedEvent;
import com.sparta.postapi.event.PostDeletedEvent;
import com.sparta.postapi.event.PostEventPublisher;
import com.sparta.postapi.event.PostUpdatedEvent;
import com.sparta.postapi.global.response.ApiResponse;
import com.sparta.postapi.global.response.PageResponse;
import com.sparta.postapi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostEventPublisher postEventPublisher;

    @PostMapping
    public ApiResponse<Long> createPost(@Valid @RequestBody PostCreateRequest request) {
        Post post = postService.createPost(request);
        postEventPublisher.handlePostCreated(PostCreatedEvent.from(post));

        return ApiResponse.success(post.getId());
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        Post post = postService.updatePost(id, request);
        postEventPublisher.handlePostUpdated(PostUpdatedEvent.from(post));
        return ApiResponse.success(post.getId());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id) {
        Post post = postService.deletePost(id);
        postEventPublisher.handlePostDeleted(PostDeletedEvent.from(post));
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long id) {
        return ApiResponse.success(postService.getPost(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<PostResponse>> getPosts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(PageResponse.from(postService.getPosts(pageable)));
    }
}
