package com.example.MyMindMate.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignUpRequest {    private String account;
    private String email;
    private String password;
    private String passwordConfirm;

    public UserDto toUserDto() {
        return UserDto.builder()
                .account(account)
                .email(email)
                .password(password)
                .build();
    }
}
