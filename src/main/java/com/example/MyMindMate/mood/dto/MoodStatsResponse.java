package com.example.MyMindMate.mood.dto;

import com.example.MyMindMate.mood.MoodTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MoodStatsResponse {
    private MoodTypeName moodTypeName;
    private long count;
}
