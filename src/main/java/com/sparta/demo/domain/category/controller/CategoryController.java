package com.sparta.demo.domain.category.controller;

import com.sparta.demo.domain.category.dto.CategoryRequest;
import com.sparta.demo.domain.category.dto.CategoryResponse;
import com.sparta.demo.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "카테고리 관리 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "카테고리 전체 조회", description = "모든 카테고리 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> responseList = categoryService.getAllCategories();
        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "카테고리 수정", description = "기존 카테고리 정보를 수정합니다.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }
}
