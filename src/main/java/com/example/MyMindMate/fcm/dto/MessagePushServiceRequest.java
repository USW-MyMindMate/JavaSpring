package com.example.MyMindMate.fcm.dto;


import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagePushServiceRequest {
    private String targetToken; // 푸시 알림을 받을 사용자의 디바이스 토큰
    private String title; // 알림 제목
    private String body; //알림 내용
}
