package com.sparta.openai.ai.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Usage(
        @JsonProperty("prompt_tokens")
        Integer promptTokens,
        @JsonProperty("completion_tokens")
        Integer completionTokens,
        @JsonProperty("total_tokens")
        Integer totalTokens
) {
    public static Usage of(int promptTokens, int completionTokens) {
        return new Usage(
                promptTokens,
                completionTokens,
                promptTokens + completionTokens
        );
    }
}