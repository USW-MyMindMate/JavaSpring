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
    private String parent_Account;  // 부모 User의 id만 담음
    private String child_Account;
    private LocalDate birthdate;
}
