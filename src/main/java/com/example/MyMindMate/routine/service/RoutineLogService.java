package com.example.MyMindMate.routine.service;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.RoutineLog;
import com.example.MyMindMate.routine.dto.RoutineLogRequest;
import com.example.MyMindMate.routine.dto.RoutineLogResponse;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import com.example.MyMindMate.routine.repository.RoutineRepository;
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
    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;

    @Transactional
    public RoutineLogResponse updateIsCompleted(RoutineLogRequest request) {
        Routine routine = routineRepository.findById(request.getRoutineId())
                .orElseThrow(() -> new IllegalArgumentException("해당 루틴을 찾을 수 없습니다."));

        RoutineLog routineLog = routineLogRepository.findByRoutine(routine)
                .orElseThrow(() -> new IllegalArgumentException("해당 루틴 로그를 찾을 수 없습니다."));

        routineLog.update(request.isCompleted());
        routineLogRepository.save(routineLog);

        return RoutineLogResponse.builder()
                .routineId(routine.getId())
                .isCompleted(routineLog.isCompleted())
                .build();
    }

//    //  account 기반 루틴 로그 조회
//    public List<RoutineLogResponse> getLogsByAccount(String account) {
//        User user = userRepository.findByAccount(account)
//                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
//        Long userId = user.getId();
//
//        return routineLogRepository.findByUserId(userId).stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
    
}
