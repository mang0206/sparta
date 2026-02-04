package com.sparta.postapi.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    private String message;
    private T data;
    private String error;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return ApiResponse.<T>builder()
                .message("Error")
                .error(errorMessage)
                .build();
    }
}
