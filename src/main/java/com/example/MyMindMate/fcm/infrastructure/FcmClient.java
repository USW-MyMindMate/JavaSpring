package com.example.MyMindMate.fcm.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class FcmClient {

    private final AccessTokenProvider accessTokenProvider;

    @Value("${fcm.url}")
    private String fcmSendUrl;

    private final RestClient rest = RestClient.create();

    public void send(String jsonBody) {
        String bearer = "Bearer " + accessTokenProvider.getAccessToken();

        rest.post()
                .uri(fcmSendUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", bearer)
                .body(jsonBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new IllegalStateException("[FCM] 4xx 오류: " + res.getStatusCode());
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new IllegalStateException("[FCM] 5xx 오류: " + res.getStatusCode());
                })
                .toBodilessEntity();
    }
}
