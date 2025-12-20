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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductExternalService {
    private final ExternalShopClient externalShopClient;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void saveAllExternalProducts() {
        int page = 0;
        int pageSize = 10;
        boolean lastPage = false;
        int failCount = 0;

        while (!lastPage) {
            try {
            ExternalProductResponse responses = externalShopClient.getProducts(page, pageSize);
            log.info(" Respnse for page {} : {}", page, responses);

            if (responses == null || responses.getMessage() == null) {
                throw new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT);
            }

            List<ExternalProductResponse.ExternalResponse> contents = responses.getMessage()
                    .getContents();

            if (contents == null || contents.isEmpty()) {
                break;
            }

            Category category = categoryRepository.findById(1L)
                    .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_CATEGORY));

            List<Product> products = new ArrayList<>();
            for (ExternalProductResponse.ExternalResponse externalProduct : contents) {
                Product product = Product.builder()
                        .externalProductId(externalProduct.getId())
                        .name(externalProduct.getName())
                        .description(externalProduct.getDescription())
                        .stock(externalProduct.getStock())
                        .price(externalProduct.getPrice())
                        .category(category)
                        .build();
                products.add(product);
            }

            savePageData(products);

            ExternalProductResponse.ExternalPageable pageable = responses.getMessage().getPageable();
            if (pageable != null) {
                lastPage = pageable.isLast();
            } else {
                lastPage = contents.size() < pageSize;
            }
            page++;
            } catch (DomainException e) {
                failCount++;
                log.error("Page {} 동기화 실패 (시도 횟수: {}/3)", page, failCount, e);

                if (failCount >= 3) {
                    log.error("Page {} 3회 연속 실패. 해당 페이지는 건너뛰고 다음으로 진행합니다.", page);
                    page++;       // 3번 넘게 실패하면 강제로 다음 페이지로
                    failCount = 0; // 카운트 초기화
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
    void savePageData(List<Product> exturnalProducts) {
        for (Product exturnalProduct : exturnalProducts) {
            Optional<Product> externalProduct = productRepository.findByExternalProductId(exturnalProduct.getExternalProductId());
            try {
                if (externalProduct.isPresent()) {
                    Product product = externalProduct.get();
                    product.setCategory(exturnalProduct.getCategory());
                    product.setPrice(exturnalProduct.getPrice());
                    product.setStock(exturnalProduct.getStock());
                } else {
                    productRepository.save(exturnalProduct);
                }
            } catch (DomainException e) {
                log.error("저장 실패 상품 -> {}:{}", exturnalProduct.getExternalProductId(), exturnalProduct.getName());
            }
        }
    }

}
