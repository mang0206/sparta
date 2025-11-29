package com.sparta.openai.ai.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.List;

public record ModelsResponse(
        String object,
        List<ModelData> data
) {
    public static ModelsResponse of(Collection<String> modelIds) {
        List<ModelData> models = modelIds.stream()
                .sorted()
                .map(ModelData::of)
                .toList();

        return new ModelsResponse("list", models);
    }

    public record ModelData(
            String id,
            String object,
            @JsonProperty("owned_by")
            String ownedBy
    ) {
        private static ModelData of(String id) {
            return new ModelData(id, "model", "system");
        }
    }
}