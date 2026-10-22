package com.example.MyMindMate.fcm.service;

import com.example.MyMindMate.fcm.dto.MessagePushRequest;
import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.fcm.infrastructure.FcmClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

    private final FcmClient fcmClient;
    private final ObjectMapper objectMapper;

    @Value("${app.fcm.mock:false}")
    private boolean mockMode;

    // FCM 알림 전송
    public void sendNotification(MessagePushServiceRequest request) {
        try {
            // mock 모드일 경우 실제 전송 안 함
            if (mockMode) {
                log.info("[FCM-MOCK] title='{}', body='{}', token='{}'",
                        request.getTitle(), request.getBody(), request.getTargetToken());
                return;
            }

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

            log.info("[FCM] 전송 성공: {}", request.getTargetToken());
        } catch (Exception e) {
            log.error("[FCM] 전송 실패", e);
            throw new RuntimeException("[FCM] 전송 실패", e);
        }
    }
}
