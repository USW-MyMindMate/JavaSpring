package com.example.MyMindMate.routine.controller;

import com.example.MyMindMate.routine.dto.RoutineRequest;
import com.example.MyMindMate.routine.dto.RoutineResponse;
import com.example.MyMindMate.routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    public ResponseEntity<RoutineResponse> createRoutine(@RequestBody RoutineRequest request) {
        return ResponseEntity.ok(routineService.createRoutine(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutineResponse> updateRoutine(
            @PathVariable("id") Long id,
            @RequestBody RoutineRequest request) {
        return ResponseEntity.ok(routineService.updateRoutine(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable("id") Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * account 기반으로 루틴 조회
     * 예: GET /api/routines?account=child001
     */
    @GetMapping
    public ResponseEntity<List<RoutineResponse>> getRoutines(@RequestParam("account") String account) {
        return ResponseEntity.ok(routineService.getRoutineByAccount(account));
    }
}
