package com.sparta.openai.ai.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Choice(
        Integer index,
        Message message,
        @JsonProperty("finish_reason")
        String finishReason
) {
    public static Choice of(String content) {
        return Choice.builder()
                .index(0)
                .message(Message.assistant(content))
                .finishReason("stop")
                .build();
    }

    public static Choice of(Message message, String finishReason) {
        return Choice.builder()
                .index(0)
                .message(message)
                .finishReason(finishReason)
                .build();
    }

    public static Choice of(String content, String finishReason) {
        return Choice.builder()
                .index(0)
                .message(Message.assistant(content))
                .finishReason(finishReason)
                .build();
    }
}