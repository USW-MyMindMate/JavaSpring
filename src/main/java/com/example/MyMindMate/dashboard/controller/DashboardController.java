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
     */
    @GetMapping("/user")
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam("account") String account) {
        DashboardResponse response = dashboardService.getDashboardData(account);
        return ResponseEntity.ok(response);
    }
}

