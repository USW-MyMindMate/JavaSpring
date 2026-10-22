package com.example.MyMindMate.dashboard.dto;

import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class DashboardResponse {

    // 루틴 통계
    private Map<String, Object> routineStats;

    // 감정 통계
    private List<MoodStatsResponse> moodStats;

}
