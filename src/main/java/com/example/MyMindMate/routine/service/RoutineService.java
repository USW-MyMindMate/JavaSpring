package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.routine.DayOfWeekType;
import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.RoutineLog;
import com.example.MyMindMate.routine.dto.RoutineRequest;
import com.example.MyMindMate.routine.dto.RoutineResponse;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;
    private final UserRepository userRepository; //  account → userId 변환용

    // ------------------- 루틴 CRUD -------------------
    public RoutineResponse createRoutine(RoutineRequest request) {

        User childaccount = userRepository.findByAccount(request.getChildAccount())
                .orElseThrow(() -> new IllegalArgumentException("해당 자녀 계정을 찾을 수 없습니다."));

        // 루틴 생성 및 저장
        Routine routine = Routine.builder()
                .user(childaccount)
                .title(request.getTitle())
                .time(LocalTime.parse(request.getTime()))
                .dayOfWeek(request.getDayOfWeek())
                .build();

        Routine savedRoutine = routineRepository.save(routine);

        // 응답 DTO 변환 후 반환
        return RoutineResponse.builder()
                .id(savedRoutine.getId())
                .childAccount(savedRoutine.getUser().getAccount())
                .title(savedRoutine.getTitle())
                .time(savedRoutine.getTime().toString())
                .dayOfWeek(savedRoutine.getDayOfWeek())
                .build();
    }

    public RoutineResponse updateRoutine(Long id, RoutineRequest request) {

        // 1기존 루틴 찾기
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 루틴을 찾을 수 없습니다."));

        if (request.getTitle() != null) {
            routine.setTitle(request.getTitle());
        }
        if (request.getTime() != null) {
            routine.setTime(LocalTime.parse(request.getTime()));
        }
        if (request.getDayOfWeek() != null) {
            routine.setDayOfWeek(request.getDayOfWeek());
        }

        // 4️⃣ DB 반영
        Routine updatedRoutine = routineRepository.save(routine);

        // 5️⃣ 응답 DTO 변환
        return RoutineResponse.builder()
                .id(updatedRoutine.getId())
                .childAccount(updatedRoutine.getUser().getAccount())
                .title(updatedRoutine.getTitle())
                .time(updatedRoutine.getTime().toString())
                .dayOfWeek(updatedRoutine.getDayOfWeek())
                .build();
    }

    public void deleteRoutine(Long id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 루틴을 찾을 수 없습니다."));
        routineRepository.delete(routine);
    }

    //  account 기반으로 루틴 조회
    public List<RoutineResponse> getRoutineByAccount(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        Long userId = user.getId();

        return routineRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ------------------- 루틴 통계 -------------------
    public Map<String, Object> getRoutineStats(Long userId) {
        List<Routine> routines = routineRepository.findByUserId(userId);
        List<RoutineLog> logs = routineLogRepository.findByUserId(userId);

        long total = routines.size();
        long completed = logs.stream().filter(RoutineLog::isCompleted).count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRoutines", total);
        stats.put("completedRoutines", completed);
        stats.put("completionRate", total == 0 ? 0 : (completed * 100.0 / total));

        return stats;
    }

    // ------------------- 내부 유틸 -------------------
    public RoutineResponse toResponse(Routine routine) {
        return RoutineResponse.builder()
                .id(routine.getId())
                .title(routine.getTitle())
                .time(routine.getTime().toString())
                .dayOfWeek(String.valueOf(routine.getDayOfWeek()))
                .build();
    }

    private void validateRequest(RoutineRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("루틴 제목은 필수 항목입니다.");
        }

        try {
            LocalTime.parse(request.getTime());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("시간 형식이 잘못되었습니다. 예: 07:30");
        }

        String[] days = request.getDayOfWeek().split(",");
        for (String day : days) {
            try {
                DayOfWeek.valueOf(day.trim());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 요일 값입니다: " + day);
            }
        }
    }
}
