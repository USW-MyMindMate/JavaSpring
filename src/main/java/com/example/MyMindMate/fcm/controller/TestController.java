package com.example.MyMindMate.fcm.controller;

import com.example.MyMindMate.fcm.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final TestService testService;

    /**
     * account 기반 테스트 알림
     * 예: GET /api/v1/test/alarm?account=child001
     */
    @GetMapping("/alarm")
    public ResponseEntity<String> alarmTest(@RequestParam String account) {
        testService.pushToParentByChildAccount(account, "테스트 알림", "푸시 알림 테스트 중입니다");
        return ResponseEntity.ok("푸시 알림 전송 완료");
    }
}
