package com.sparta.elasticapi.elasticsearch.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSearchRequest {
    private String keyword;
}
