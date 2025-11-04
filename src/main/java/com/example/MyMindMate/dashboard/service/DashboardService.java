package com.example.MyMindMate.dashboard.service;

import com.example.MyMindMate.dashboard.dto.DashboardResponse;
import com.example.MyMindMate.mood.Mood;
import com.example.MyMindMate.mood.repository.MoodRepository;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;
    private final MoodRepository moodRepository;

    public DashboardResponse getDashboardData(Long userId) {

        // 루틴 통계
        int totalRoutines = routineRepository.findByUserId(userId).size();
        long completedRoutines = routineLogRepository.findByUserId(userId)
                .stream().filter(log -> log.isCompleted()).count();
        double completionRate = totalRoutines == 0 ? 0.0 :
                ((double) completedRoutines / totalRoutines) * 100;

        Map<String, Object> routineStats = new HashMap<>();
        routineStats.put("totalRoutines", totalRoutines);
        routineStats.put("completedRoutines", completedRoutines);
        routineStats.put("completionRate", completionRate);

        // 감정 통계
        var moodStats = moodRepository.countByMoodTypeNameGrouped(userId);

        // 추천 메시지
        List<Mood> recentMoods = moodRepository.findTop3ByUserIdOrderByRecordedAtDesc(userId);
        boolean allNegative = recentMoods.size() >= 3 &&
                recentMoods.stream().allMatch(m ->
                        m.getMoodTypeName() != null && m.getMoodTypeName().isNegative());

        String recommendation = null;
        if (allNegative) {
            List<String> messages = new ArrayList<>(List.of(
                    "기분이 자주 우울하신가요? 따뜻한 차 한 잔으로 잠시 쉬어가세요 ☕",
                    "힘든 감정이 계속될 땐, 가벼운 산책으로 마음을 환기시켜보세요 🌿",
                    "지금 감정이 반복되고 있네요. 좋아하는 음악을 들으며 기분을 풀어보는 건 어때요? 🎧"
            ));
            Collections.shuffle(messages);
            recommendation = messages.get(0);
        }


        // DashboardResponse 객체로 반환
        return DashboardResponse.builder()
                .routineStats(routineStats)
                .moodStats(moodStats)
                .recommendation(recommendation)
                .build();
    }
}
