package com.sparta.demo.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ServiceExceptionCode {
    NOT_FOUND_PRODUCT("상품을 찾을 수 없습니다."),
    INSUFFICIENT_STOCK("상품의 재고가 부족합니다."),

    NOT_FOUND_USER("사용자를 찾을 수 없습니다."),
    ALREADY_EXISTS_USER("이미 사용하고 있는 계정이 존재합니다");

    final String message;
}