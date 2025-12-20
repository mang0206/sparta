package com.sparta.msa.project_part_3.global.external.product.client;

import com.sparta.msa.project_part_3.global.config.OpenFeignConfig;
import com.sparta.msa.project_part_3.global.external.product.dto.ExternalProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "external-product",
        url = "${external.external-shop.url}",
        configuration = OpenFeignConfig.class
)
public interface ExternalShopClient {
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @GetMapping("/products?page={page}&size={size}")
    ExternalProductResponse getProducts(
            @PathVariable("page") int page,
            @PathVariable("size") int size
    );

}
