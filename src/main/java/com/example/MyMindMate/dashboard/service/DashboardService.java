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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;
    private final MoodRepository moodRepository;
    private final UserRepository userRepository;

    /**
     * 부모 account면 자녀 전체 통계, 자녀 account면 본인 통계 반환
     */
    public DashboardResponse getDashboardData(String account) {
        //  account로 유저 조회
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));

        List<Long> targetUserIds;

        //  부모면 자녀 전체, 자녀면 본인 ID만 포함
        if (user.getParent() == null) {
            // 부모 계정 → 자녀 리스트 조회
            List<User> children = userRepository.findByParent_Id(user.getId());
            targetUserIds = children.stream().map(User::getId).toList();
        } else {
            // 자녀 계정 → 본인만 조회
            targetUserIds = List.of(user.getId());
        }

        // 루틴 통계 계산
        int totalRoutines = routineRepository.countByUserIdIn(targetUserIds);
        long completedRoutines = routineLogRepository.countByUserIdInAndIsCompletedTrue(targetUserIds);

        double completionRate = totalRoutines == 0 ? 0.0 :
                ((double) completedRoutines / totalRoutines) * 100;

        Map<String, Object> routineStats = new HashMap<>();
        routineStats.put("totalRoutines", totalRoutines);
        routineStats.put("completedRoutines", completedRoutines);
        routineStats.put("completionRate", completionRate);

        //  감정 통계 조회
        var moodStats = moodRepository.countByMoodTypeNameGroupedForUserIds(targetUserIds);

        //  응답 구성
        return DashboardResponse.builder()
                .routineStats(routineStats)
                .moodStats(moodStats)
                .build();
    }
}
