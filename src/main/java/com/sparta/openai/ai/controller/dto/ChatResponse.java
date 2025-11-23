package com.sparta.openai.ai.controller.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ChatResponse(
        String id,              // 응답 고유 ID (예: "chatcmpl-abc123")
        String object,          // 객체 타입 (항상 "chat.completion")
        Long created,           // Unix timestamp (초 단위)
        String model,           // 사용된 모델 이름 (예: "qwen3")
        List<Choice> choices,   // AI 응답 선택지 배열 (보통 1개)
        Usage usage            // 토큰 사용량 정보
) {
    public static ChatResponse of(String id, String model,
                                            String content, Usage usage) {
        return ChatResponse.builder()
                .id(id)
                .object("chat.completion")
                .created(System.currentTimeMillis() / 1000)
                .model(model)
                .choices(List.of(Choice.of(content)))
                .usage(usage)
                .build();
    }

    public static ChatResponse of(String model, String content, Usage usage) {
        return of("chatcmpl-" + UUID.randomUUID(), model, content, usage);
    }

    public static ChatResponse of(String model, String content) {
        return of(model, content, null);
    }
}