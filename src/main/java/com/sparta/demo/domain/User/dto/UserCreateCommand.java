package com.sparta.demo.domain.User.dto;

public record UserCreateCommand(String username, String email, String password) {
}

