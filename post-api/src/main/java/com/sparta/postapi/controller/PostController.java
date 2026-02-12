package com.sparta.postapi.controller;

import com.sparta.postapi.dto.PostCreateRequest;
import com.sparta.postapi.dto.PostResponse;
import com.sparta.postapi.dto.PostUpdateRequest;
import com.sparta.postapi.entity.Post;
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

    @PostMapping
    public ApiResponse<Long> createPost(@Valid @RequestBody PostCreateRequest request) {
        Long postId = postService.createPost(request);

        return ApiResponse.success(postId);
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        Long postId = postService.updatePost(id, request);

        return ApiResponse.success(postId);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);

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
