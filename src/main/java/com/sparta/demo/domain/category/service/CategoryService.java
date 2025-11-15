package com.sparta.demo.domain.category.service;

import com.sparta.demo.domain.category.dto.CategoryRequest;
import com.sparta.demo.domain.category.dto.CategoryResponse;
import com.sparta.demo.domain.category.entity.Category;
import com.sparta.demo.domain.category.repository.CategoryRepository;

import com.sparta.demo.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    // 1. 카테고리 등록
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        // (선택) 이름 중복 검사
        categoryRepository.findByName(request.getName()).ifPresent(c -> {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        });
        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 카테고리 ID입니다."));
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parent)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(savedCategory);
    }

    // 2. 카테고리 전체 조회
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    // 3. 카테고리 수정
    @Transactional
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));

        // 수정하려는 이름이 이미 존재하는지
        categoryRepository.findByName(request.getName()).ifPresent(c -> {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        });

        Category parent = null;
        if (request.getParentId() != null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부모 카테고리 ID입니다."));
        }

        category.update(request.getName(), request.getDescription(), parent);
        // @Transactional에 의해 메서드 종료 시 자동 더티 체킹(Dirty Checking) 및 DB 업데이트
        return new CategoryResponse(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));

        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("하위 카테고리가 존재하여 삭제할 수 없습니다.");
        }

        if (productRepository.existsByCategory(category)) {
            throw new IllegalStateException("카테고리에 속한 상품이 존재하여 삭제할 수 없습니다.");
        }

        categoryRepository.delete(category);
    }

}
