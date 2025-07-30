package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.routine.RoutineLog;
import com.example.MyMindMate.routine.dto.RoutineLogRequest;
import com.example.MyMindMate.routine.dto.RoutineLogResponse;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineLogService {

    private final RoutineLogRepository routineLogRepository;

    @Transactional
    public RoutineLogResponse createOrUpdateLog(RoutineLogRequest request) {
        System.out.println("📥 전달받은 request.isCompleted: " + request.isCompleted());

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
            System.out.println("🔁 기존 로그 상태 (수정 전): " + log.isCompleted());
            log.update(request.isCompleted());
            log = routineLogRepository.save(log);
            System.out.println("✅ 수정 완료 상태: " + log.isCompleted());
        }

        return toResponse(log);
    }

    public List<RoutineLogResponse> getLogsByUserId(Long userId) {
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
