package com.sparta.demo.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionChunk {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<ChatChoiceChunk> choices;
}
