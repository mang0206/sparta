package com.sparta.demo.domain.category.dto;

import com.sparta.demo.domain.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CategoryResponse {

    @Schema(description = "카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 이름", example = "전자제품")
    private String name;

    @Schema(description = "카테고리 설명", example = "가전, 모바일, PC 등")
    private String description;

    // Entity to DTO
    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
    }
}