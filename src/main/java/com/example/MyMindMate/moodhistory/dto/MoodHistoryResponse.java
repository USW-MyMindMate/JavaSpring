package com.example.MyMindMate.moodhistory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MoodHistoryResponse {

    // 날짜를 문자열 형태로 변환
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordedAt; // 감정 기록 시간

    private String moodTypeName;      // 감정 이름
    private String reason;            // 이유 (null 가능)
}
