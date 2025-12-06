package com.sparta.msa.project_part_3.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password; // 로그인 시 비밀번호는 필수이므로 추가했습니다.

}