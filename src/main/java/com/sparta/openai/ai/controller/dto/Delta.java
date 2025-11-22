package com.sparta.openai.ai.controller.dto;

import lombok.Builder;

@Builder
public record Delta(
        String role,
        String content
) {
    public static Delta role(String role) {
        return Delta.builder().role(role).build();
    }

    public static Delta content(String content) {
        return Delta.builder().content(content).build();
    }
}