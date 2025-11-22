package com.sparta.openai.ai.controller.dto;

import lombok.Builder;

@Builder
public record Message(
        String role,
        String content
) {
    public static Message system(String content) {
        return Message.builder()
                .role("system")
                .content(content)
                .build();
    }

    public static Message user(String content) {
        return Message.builder()
                .role("user")
                .content(content)
                .build();
    }

    public static Message assistant(String content) {
        return Message.builder()
                .role("assistant")
                .content(content)
                .build();
    }
}