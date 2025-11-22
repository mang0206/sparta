package com.sparta.demo.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatChoice {
    private Integer index;
    private ChatMessage message;

    @JsonProperty("finish_reason")
    private String finishReason;
}
