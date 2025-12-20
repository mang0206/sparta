package com.sparta.msa.project_part_3.global.schedule;

import com.sparta.msa.project_part_3.domain.product.service.ProductExternalService;
import com.sparta.msa.project_part_3.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSchedule {
    private final ProductExternalService productExternalService;

    @Scheduled(cron = "0 0 * * * *")
    public void runExternalProduct() {
        log.info("[외부 상품 동기화 스케줄링 실행]");
        productExternalService.saveAllExternalProducts();
    }

}
