package com.sparta.msa.project_part_3.domain.product.service;

import com.sparta.msa.project_part_3.domain.category.entity.Category;
import com.sparta.msa.project_part_3.domain.category.repository.CategoryRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import com.sparta.msa.project_part_3.global.external.product.client.ExternalShopClient;
import com.sparta.msa.project_part_3.global.external.product.dto.ExternalProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSyncExecutor {
    private final ExternalShopClient externalShopClient;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Retryable(
            retryFor = {DomainException.class, RuntimeException.class},
            maxAttempts = 4, // 1회 실행 + 3회 재시도
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public boolean syncPage(int page, int pageSize) {
        log.info("외부 상품 동기화 시도 - Page: {}", page);

        ExternalProductResponse responses = externalShopClient.getProducts(page, pageSize);

        if (responses == null || responses.getMessage() == null) {
            throw new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT);
        }

        List<ExternalProductResponse.ExternalResponse> contents = responses.getMessage().getContents();

        if (contents == null || contents.isEmpty()) {
            return false;
        }

        Category category = categoryRepository.findById(1L)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_CATEGORY));

        List<Product> productsToSave = new ArrayList<>();
        for (ExternalProductResponse.ExternalResponse externalProduct : contents) {
            Product product = Product.builder()
                    .externalProductId(externalProduct.getId())
                    .name(externalProduct.getName())
                    .description(externalProduct.getDescription())
                    .stock(externalProduct.getStock())
                    .price(externalProduct.getPrice())
                    .category(category)
                    .build();
            productsToSave.add(product);
        }

        // DB Upsert 수행
        upsertProducts(productsToSave);

        // 다음 페이지 여부 확인
        ExternalProductResponse.ExternalPageable pageable = responses.getMessage().getPageable();
        if (pageable != null) {
            return !pageable.isLast();
        } else {
            return contents.size() >= pageSize;
        }
    }

    /**
     * 기존 상품이 있으면 업데이트, 없으면 저장
     */
    private void upsertProducts(List<Product> newProducts) {
        for (Product newProduct : newProducts) {
            Optional<Product> existingProductOpt = productRepository.findByExternalProductId(newProduct.getExternalProductId());

            if (existingProductOpt.isPresent()) {
                Product existingProduct = existingProductOpt.get();
                existingProduct.setCategory(newProduct.getCategory());
                existingProduct.setName(newProduct.getName());
                existingProduct.setDescription(newProduct.getDescription());
                existingProduct.setPrice(newProduct.getPrice());
                existingProduct.setStock(newProduct.getStock());
            } else {
                productRepository.save(newProduct);
            }
        }
    }
}
