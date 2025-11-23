package com.sparta.openai.ai.controller.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatChunk (
        String id,
        String object,
        Long created,
        String model,
        List<ChatChunkChoice> choices
) {
    public static ChatChunk first(String id, Long created, String model) {
        return ChatChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(created)
                .model(model)
                .choices(List.of(ChatChunkChoice.of(Delta.role("assistant"))))
                .build();
    }

    public static ChatChunk content(String id, Long created, String model, String content) {
        return ChatChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(created)
                .model(model)
                .choices(List.of(ChatChunkChoice.of(Delta.content(content))))
                .build();
    }

    public static ChatChunk stop(String id, Long created, String model) {
        return ChatChunk.builder()
                .id(id)
                .object("chat.completion.chunk")
                .created(created)
                .model(model)
                .choices(List.of(ChatChunkChoice.stop()))
                .build();
    }
}