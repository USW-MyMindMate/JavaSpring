package com.example.MyMindMate.mood.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodResponse {

    private Long userId;
    private String account;
    private String moodType;
    private String reason;
    private LocalDateTime recordedAt;

}
