package com.example.MyMindMate.dashboard.service;

import com.example.MyMindMate.dashboard.dto.DashboardResponse;
import com.example.MyMindMate.mood.repository.MoodRepository;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;
    private final MoodRepository moodRepository;

    public DashboardResponse getDashboardData(Long userId) {

        // 루틴 통계 계산
        int totalRoutines = routineRepository.findByUserId(userId).size();
        long completedRoutines = routineLogRepository.findByUserId(userId)
                .stream().filter(log -> log.isCompleted()).count();

        double completionRate = totalRoutines == 0 ? 0.0 :
                ((double) completedRoutines / totalRoutines) * 100;

        Map<String, Object> routineStats = new HashMap<>();
        routineStats.put("totalRoutines", totalRoutines);
        routineStats.put("completedRoutines", completedRoutines);
        routineStats.put("completionRate", completionRate);

        // 감정 통계 (그래프용 데이터)
        var moodStats = moodRepository.countByMoodTypeNameGrouped(userId);

        // 활동 추천 제거 — 부모 대시보드는 감정 누적 그래프만 표시
        return DashboardResponse.builder()
                .routineStats(routineStats)
                .moodStats(moodStats)
                .build();
    }
}
