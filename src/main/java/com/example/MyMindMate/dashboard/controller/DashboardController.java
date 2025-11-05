package com.example.MyMindMate.dashboard.controller;

import com.example.MyMindMate.dashboard.dto.DashboardResponse;
import com.example.MyMindMate.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 프론트에서 account로 대시보드 조회
     * 예: GET /api/dashboard?account=child001
     */
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam String account) {
        return ResponseEntity.ok(dashboardService.getDashboardData(account));
    }
}

