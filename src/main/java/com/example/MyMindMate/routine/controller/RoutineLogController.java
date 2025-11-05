package com.example.MyMindMate.routine.controller;

import com.example.MyMindMate.global.ApiResponse;
import com.example.MyMindMate.routine.dto.RoutineLogRequest;
import com.example.MyMindMate.routine.dto.RoutineLogResponse;
import com.example.MyMindMate.routine.service.RoutineLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routine-logs")
public class RoutineLogController {

    private final RoutineLogService routineLogService;

    // 루틴 체크 or 해제
    @PutMapping
    public ResponseEntity<ApiResponse> updateRoutineLog(@RequestBody RoutineLogRequest request) {
        RoutineLogResponse routineLogResponse = routineLogService.updateIsCompleted(request);
        return ResponseEntity.ok(new ApiResponse(routineLogResponse+ "루틴 변경되었습니다."));
    }


    /**
     * account 기반 루틴 로그 조회
     * 예: GET /api/routine-logs?account=child001
     */
//    @GetMapping
//    public ResponseEntity<List<RoutineLogResponse>> getRoutineLogsByAccount(@RequestParam("account") String account) {
//        List<RoutineLogResponse> logs = routineLogService.getLogsByAccount(account);
//        return ResponseEntity.ok(logs);
//    }
}
