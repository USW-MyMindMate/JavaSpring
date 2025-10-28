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

    private Long userId;
    private String reason;
    private String moodTypeName; // 감정 이름 (선택적으로 추가 가능)
}
