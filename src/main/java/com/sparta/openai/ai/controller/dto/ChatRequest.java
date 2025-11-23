package com.sparta.openai.ai.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ChatRequest(
        String model,
        List<Message> messages,
        Double temperature,
        @JsonProperty("max_tokens")
        Integer maxTokens,
        Boolean stream
) {
    public static ChatRequest of(String model, List<Message> messages) {
        return ChatRequest.builder()
                .model(model)
                .messages(messages)
                .temperature(0.7)
                .maxTokens(500)
                .stream(false)
                .build();
    }

    public static ChatRequest of(String model, List<Message> messages, Boolean stream) {
        return ChatRequest.builder()
                .model(model)
                .messages(messages)
                .temperature(0.7)
                .maxTokens(500)
                .stream(stream)
                .build();
    }
}
