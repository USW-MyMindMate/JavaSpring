package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
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
    private final UserRepository userRepository; //  추가

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

    //  account 기반 루틴 로그 조회
    public List<RoutineLogResponse> getLogsByAccount(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        Long userId = user.getId();

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
