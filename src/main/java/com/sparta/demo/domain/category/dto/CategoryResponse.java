package com.sparta.demo.domain.category.dto;

import com.sparta.demo.domain.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryResponse {

    @Schema(description = "카테고리 ID", example = "1")
    private Long id;

    @Schema(description = "카테고리 이름", example = "전자제품")
    private String name;

    @Schema(description = "카테고리 설명", example = "가전, 모바일, PC 등")
    private String description;

    @Schema(description = "부모 카테고리 ID")
    private Long parentId;

    @Schema(description = "자식 카테고리 목록")
    private List<CategoryResponse> children;

    // Entity to DTO
    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        if (category.getParent() != null) {
            this.parentId = category.getParent().getId();
        }
        this.children = category.getChildren().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }
}