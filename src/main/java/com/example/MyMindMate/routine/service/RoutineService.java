package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.RoutineLog;
import com.example.MyMindMate.routine.dto.RoutineRequest;
import com.example.MyMindMate.routine.dto.RoutineResponse;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import com.example.MyMindMate.routine.repository.RoutineRepository;
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

    // ------------------- 루틴 CRUD -------------------
    public RoutineResponse createRoutine(RoutineRequest request) {
        validateRequest(request);

        Routine routine = Routine.builder()
                .userId(request.getUserId())
                .createBy(request.getCreatedBy())
                .title(request.getTitle())
                .time(LocalTime.parse(request.getTime()))
                .dayOfWeek(request.getDayOfWeek())
                .build();

        routine = routineRepository.save(routine);
        return toResponse(routine);
    }

    public RoutineResponse updateRoutine(Long id, RoutineRequest request) {
        validateRequest(request);

        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("루틴을 찾을 수 없습니다."));

        routine = Routine.builder()
                .id(id)
                .userId(request.getUserId())
                .createBy(request.getCreatedBy())
                .title(request.getTitle())
                .time(LocalTime.parse(request.getTime()))
                .dayOfWeek(request.getDayOfWeek())
                .build();

        return toResponse(routineRepository.save(routine));
    }

    public void deleteRoutine(Long id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("삭제할 루틴이 존재하지 않습니다."));
        routineRepository.deleteById(id);
    }

    public List<RoutineResponse> getRoutineByUserId(Long userId) {
        return routineRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ------------------- 루틴 통계(7번 기능 핵심) -------------------
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
                .dayOfWeek(routine.getDayOfWeek())
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
