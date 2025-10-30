package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.dto.RoutineRequest;
import com.example.MyMindMate.routine.dto.RoutineResponse;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

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
