package com.sparta.openai.ai.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ChatChunkChoice (
        Integer index,
        Delta delta,
        @JsonProperty("finish_reason")
        String finishReason
) {
    public static ChatChunkChoice of(Delta delta) {
        return ChatChunkChoice.builder()
                .index(0)
                .delta(delta)
                .finishReason(null)
                .build();
    }

    public static ChatChunkChoice stop() {
        return ChatChunkChoice.builder()
                .index(0)
                .delta(Delta.builder().build())
                .finishReason("stop")
                .build();
    }
}
