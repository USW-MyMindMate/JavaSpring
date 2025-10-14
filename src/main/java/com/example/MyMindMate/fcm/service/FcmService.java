package com.example.MyMindMate.fcm.service;

import com.example.MyMindMate.fcm.dto.MessagePushRequest;
import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.fcm.infrastructure.FcmClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmClient fcmClient;
    private final ObjectMapper objectMapper;

    // FCM 알림 전송
    public void sendNotification(MessagePushServiceRequest request) {
        try {
            MessagePushRequest message = MessagePushRequest.builder()
                    .validateOnly(false)
                    .message(
                            MessagePushRequest.MessageRequest.builder()
                                    .notification(
                                            MessagePushRequest.NotificationRequest.builder()
                                                    .title(request.getTitle())
                                                    .body(request.getBody())
                                                    .build()
                                    )
                                    .token(request.getTargetToken())
                                    .build()
                    )
                    .build();

            String messageJson = objectMapper.writeValueAsString(message);
            fcmClient.send(messageJson);
        } catch (Exception e) {
            throw new RuntimeException("[FCM] 전송 실패", e);
        }
    }
}
