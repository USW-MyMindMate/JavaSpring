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
    public ResponseEntity<RoutineResponse> updateRoutine(@PathVariable Long id, @RequestBody RoutineRequest request) {
        return ResponseEntity.ok(routineService.updateRoutine(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoutineResponse>> getRoutines(@PathVariable Long userId) {
        return ResponseEntity.ok(routineService.getRoutineByUserId(userId));
    }

    //@PatchMapping("/{id}")
    //public ResponseEntity<>
}
