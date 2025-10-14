package com.example.MyMindMate.fcm.controller;

import com.example.MyMindMate.fcm.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/alarm")
    public ResponseEntity<String> alarmTest() {
        // 자녀 사용자 ID가 1번인 경우 (예시)
        Long childUserId = 1L;
        testService.pushToParentByChildUserId(childUserId, "테스트 알림", "푸시 알림 테스트 중입니다");
        return ResponseEntity.ok("푸시 알림 전송 완료");
    }
}
