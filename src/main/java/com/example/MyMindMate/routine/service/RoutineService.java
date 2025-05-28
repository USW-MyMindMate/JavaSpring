package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.dto.RoutineRequest;
import com.example.MyMindMate.routine.dto.RoutineResponse;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineResponse createRoutine(RoutineRequest request) {
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
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다."));

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
}
