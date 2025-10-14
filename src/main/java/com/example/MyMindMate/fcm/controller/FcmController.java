package com.example.MyMindMate.fcm.controller;

import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    // 실제 알림 전송
    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody MessagePushServiceRequest request) {
        fcmService.sendNotification(request);
        return ResponseEntity.ok("알림 전송 성공");
    }
}
