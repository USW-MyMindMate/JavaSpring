package com.example.MyMindMate.member.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProfileDto {
    private Long id;
    private Long userId;  // 부모 User의 id만 담음
    private String account;
    private LocalDate birthdate;
}
