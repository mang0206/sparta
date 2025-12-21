package com.sparta.msa.project_part_3.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductExternalService {

    private final ProductSyncExecutor productSyncExecutor;

    public void saveAllExternalProducts() {
        int page = 0;
        int pageSize = 10;
        boolean hasNext = true;

        while (hasNext) {
            try {
                // 페이지 단위로 동기화 실행
                hasNext = productSyncExecutor.syncPage(page, pageSize);
            } catch (Exception e) {
                log.error("Page {} 동기화 최종 실패. 다음 페이지로 진행합니다.", page, e);
            } finally {
                page++;
            }
        }
    }




}
