package com.sns.elasticsearch.domain.post.controller;

import com.sns.elasticsearch.domain.post.dto.PostSearchResponse;
import com.sns.elasticsearch.domain.post.service.PostSearchService;
import com.sns.elasticsearch.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;

    @GetMapping("/posts")
    public ApiResponse<List<PostSearchResponse>> searchPosts(@RequestParam String q) {
        return ApiResponse.success(postSearchService.searchPosts(q));
    }
}
