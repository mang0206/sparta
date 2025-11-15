package com.sparta.demo.domain.category.dto;

import com.sparta.demo.domain.category.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryRequest {

    @Schema(description = "카테고리 이름", example = "전자제품")
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;

    @Schema(description = "카테고리 설명", example = "가전, 모바일, PC 등")
    private String description;

    @Schema(description = "부모 카테고리 ID", example = "1")
    private Long parentId;

    // DTO to Entity
    public Category toEntity() {
        return Category.builder()
                .name(this.name)
                .description(this.description)
                .build();
    }
}