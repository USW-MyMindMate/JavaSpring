package com.example.MyMindMate.member.dto;


import com.example.MyMindMate.member.domain.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String account;
    private String email;
    private String password;
    private String role;
    private Long parentId;  // 부모의 id만 가져옴
    private List<ChildProfileDto> childProfiles;  // 자녀 프로필 리스트

    public void clearPassword(){
        this.password = null;
    }

    public static UserDto loginResponse(User user) {
        return UserDto.builder()
                .id(user.getId())
                .account(user.getAccount())
                .role(user.getRole())
                .build();
    }

}