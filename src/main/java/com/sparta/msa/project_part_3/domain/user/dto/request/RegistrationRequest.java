package com.sparta.msa.project_part_3.domain.user.dto.request;

import com.sparta.msa.project_part_3.global.enums.Gender;
import lombok.Getter;

@Getter
public class RegistrationRequest {

    private String name;
    private String phone;
    private String email;
    private String password;
    private Gender gender;

}