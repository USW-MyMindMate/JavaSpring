package com.example.MyMindMate.mood.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoodRecordRequest {

    private String account;  // 변경: userId 대신 account로 받기
    private String reason;
    private String moodTypeName;
}
