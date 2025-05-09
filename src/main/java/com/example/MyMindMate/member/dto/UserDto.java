package com.example.MyMindMate.member.dto;


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
    private String password;
    private String role;
    private Long parentId;  // 부모의 id만 가져옴
    private String fcmToken;
    private List<ChildProfileDto> childProfiles;  // 자녀 프로필 리스트
}