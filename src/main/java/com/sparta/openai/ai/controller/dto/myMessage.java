package com.sparta.openai.ai.controller.dto;

import lombok.Builder;

@Builder
public record myMessage(
        String role,
        String content
) {
    public static myMessage system(String content) {
        return myMessage.builder()
                .role("system")
                .content(content)
                .build();
    }

    public static myMessage user(String content) {
        return myMessage.builder()
                .role("user")
                .content(content)
                .build();
    }

    public static myMessage assistant(String content) {
        return myMessage.builder()
                .role("assistant")
                .content(content)
                .build();
    }
}