package com.example.MyMindMate.dashboard.service;

import com.example.MyMindMate.dashboard.dto.DashboardResponse;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
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
    private final UserRepository userRepository; // account → userId 변환용

    /**
     * account 기반 대시보드 조회
     */
    public DashboardResponse getDashboardData(String account) {
        //  account → userId 변환
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        Long userId = user.getId();

        //  루틴 통계 계산
        int totalRoutines = routineRepository.findByUserId(userId).size();
        long completedRoutines = routineLogRepository.findByUserId(userId)
                .stream().filter(log -> log.isCompleted()).count();

        double completionRate = totalRoutines == 0 ? 0.0 :
                ((double) completedRoutines / totalRoutines) * 100;

        Map<String, Object> routineStats = new HashMap<>();
        routineStats.put("totalRoutines", totalRoutines);
        routineStats.put("completedRoutines", completedRoutines);
        routineStats.put("completionRate", completionRate);

        //  감정 통계
        var moodStats = moodRepository.countByMoodTypeNameGrouped(userId);

        // 대시보드 반환
        return DashboardResponse.builder()
                .routineStats(routineStats)
                .moodStats(moodStats)
                .build();
    }
}
