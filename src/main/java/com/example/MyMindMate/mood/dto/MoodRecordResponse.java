package com.example.MyMindMate.mood.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MoodRecordResponse {
    private String message;         // "감정 기록 완료" 등
    private String recommendation;  // 3회 연속 부정 감정 시 추천 문구 (없으면 null)
}
