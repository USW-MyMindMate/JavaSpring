package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.routine.RoutineLog;
import com.example.MyMindMate.routine.dto.RoutineLogRequest;
import com.example.MyMindMate.routine.dto.RoutineLogResponse;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineLogService {

    private final RoutineLogRepository routineLogRepository;

    @Transactional
    public RoutineLogResponse createOrUpdateLog(RoutineLogRequest request) {
        if (request.getRoutineId() == null || request.getUserId() == null) {
            throw new IllegalArgumentException("routineId 또는 userId가 누락되었습니다.");
        }

        RoutineLog log = routineLogRepository
                .findByRoutineIdAndUserId(request.getRoutineId(), request.getUserId())
                .orElse(null);

        if (log == null) {
            log = RoutineLog.builder()
                    .routineId(request.getRoutineId())
                    .userId(request.getUserId())
                    .isCompleted(request.isCompleted())
                    .build();
            log = routineLogRepository.save(log);
        } else {
            log.update(request.isCompleted());
            log = routineLogRepository.save(log);
        }

        return toResponse(log);
    }

    public List<RoutineLogResponse> getLogsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }

        return routineLogRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private RoutineLogResponse toResponse(RoutineLog log) {
        return RoutineLogResponse.builder()
                .id(log.getId())
                .routineId(log.getRoutineId())
                .userId(log.getUserId())
                .isCompleted(log.isCompleted())
                .build();
    }
}
