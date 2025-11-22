package com.sparta.openai.ai.controller.dto;

import lombok.Builder;

import java.util.Arrays;
import java.util.List;

@Builder
public record ModelsResponse(
        String object,
        List<ModelInfo> data
) {
    public static ModelsResponse of(List<ModelInfo> models) {
        return ModelsResponse.builder()
                .object("list")
                .data(models)
                .build();
    }

    public static ModelsResponse of(ModelInfo... models) {
        return of(Arrays.asList(models));
    }

    public static ModelsResponse single(String modelId) {
        return of(ModelInfo.of(modelId));
    }
}