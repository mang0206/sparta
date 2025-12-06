package com.sparta.msa.project_part_3.domain.user.controller;

import com.sparta.msa.project_part_3.domain.user.dto.request.LoginRequest;
import com.sparta.msa.project_part_3.domain.user.dto.request.RegistrationRequest;
import com.sparta.msa.project_part_3.domain.user.dto.response.LoginResponse;
import com.sparta.msa.project_part_3.domain.user.service.UserService;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

    @PostMapping("/registration")
    public ApiResponse<Void> registration(@Valid @RequestBody RegistrationRequest request) {
        userService.registration(request);
        return ApiResponse.ok();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                            HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = userService.login(loginRequest, request, response);
        HttpSession session= request.getSession(false);
        session.setAttribute("loginResponse", loginResponse);
        return ApiResponse.ok(loginResponse);
    }

    @GetMapping("/status")
    public ApiResponse<LoginResponse> checkStatus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new DomainException(DomainExceptionCode.NOT_FOUND_USER);
        }

        return ApiResponse.ok(userService.getLoginInfo(authentication));
    }

    @GetMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.ok();
    }

}
