package com.example.MyMindMate.mood.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodStatsResponse {

    private Long userId;
    private String account;
    private Map<MoodTypeName, Long> moodCountMap;  // 예: {좋아요=2, 슬퍼요=1, ...}

}