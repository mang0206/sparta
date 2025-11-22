package com.sparta.openai.ai.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ModelInfo(
        String id,
        String object,
        Long created,
        @JsonProperty("owned_by")
        String ownedBy
) {
    public static ModelInfo of(String id, String ownedBy) {
        return ModelInfo.builder()
                .id(id)
                .object("model")
                .created(System.currentTimeMillis() / 1000)
                .ownedBy(ownedBy)
                .build();
    }

    public static ModelInfo of(String id) {
        return of(id, "custom");
    }

    public static ModelInfo qwen3() {
        return of("qwen3", "ollama");
    }

    public static ModelInfo llama3() {
        return of("llama3", "ollama");
    }
}