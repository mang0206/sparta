package com.sparta.msa.project_part_3.global.external.product.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class ExternalProductResponse {
    Boolean result;
    ExternalError error;
    ExternalPage message;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @FieldDefaults(level =  AccessLevel.PRIVATE)
    public static class ExternalError {
        String errorCode;
        String errorMessage;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @FieldDefaults(level =  AccessLevel.PRIVATE)
    public static class ExternalPage {
        List<ExternalResponse> contents;
        ExternalPageable pageable;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ExternalPageable {
        Long offset;
        Long pageNumber;
        Long pageSize;
        Long pageElements;
        Long totalPages;
        Long totalElements;
        boolean first;
        boolean last;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ExternalResponse {
        Long id;
        String name;
        String description;
        BigDecimal price;
        Integer stock;
        LocalDateTime created;
        LocalDateTime updated;
    }

}
