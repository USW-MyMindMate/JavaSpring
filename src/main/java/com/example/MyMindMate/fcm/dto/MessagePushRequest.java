package com.example.MyMindMate.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePushRequest {
    private boolean validateOnly; // true = 테스트 전송, false = 실제 전송
    private MessageRequest message; // 실제 메시지 정보
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageRequest {
        private NotificationRequest notification; // 알림의 제목&내용
        private String token;
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotificationRequest {
        private String title; // 알림 제목
        private String body; // 알림 내용
    }
}
