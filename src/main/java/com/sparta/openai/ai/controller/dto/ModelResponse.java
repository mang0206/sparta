package com.sparta.openai.ai.controller.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ModelResponse(
        String object,
        List<ModelData> data
) {
    @Builder
    public record ModelData(
            String id,
            String object,
            long created,
            String owned_by
    ) {}
}
