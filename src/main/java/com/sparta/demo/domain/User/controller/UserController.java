package com.sparta.demo.domain.User.controller;

import com.sparta.demo.common.ApiResponse;
import com.sparta.demo.common.annotation.Loggable;
import com.sparta.demo.domain.User.controller.dto.UserCreateRequest;
import com.sparta.demo.domain.User.controller.dto.UserResponse;
import com.sparta.demo.domain.User.controller.dto.UserUpdateRequest;
import com.sparta.demo.domain.User.dto.UserDto;
import com.sparta.demo.domain.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Loggable
    @GetMapping
    public ApiResponse<List<UserResponse>> findAll() {
        return ApiResponse.success(UserResponse.from(userService.searchUser()));
    }

    @Loggable
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        UserDto created = userService.createUser(request.toCommand());
        return ApiResponse.created(UserResponse.from(created));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> findById(@PathVariable Long userId) {
        UserDto user = userService.getUserById(userId);
        return ApiResponse.success(UserResponse.from(user));
    }

    @Loggable
    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> update(@PathVariable Long userId,
                                            @Valid @RequestBody UserUpdateRequest request) {
        UserDto updated = userService.updateUser(userId, request.toCommand());
        return ApiResponse.success(UserResponse.from(updated));
    }

    @Loggable
    @DeleteMapping("/{userId}")
    public ApiResponse<Void> delete(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.success();
    }
}
