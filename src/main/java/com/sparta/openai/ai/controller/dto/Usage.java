package com.sparta.openai.ai.controller.dto;

public record Usage(
        //@JsonProperty("prompt_tokens")
        Integer promptTokens,
        //@JsonProperty("completion_tokens")
        Integer completionTokens,
        //@JsonProperty("total_tokens")
        Integer totalTokens
) {

}