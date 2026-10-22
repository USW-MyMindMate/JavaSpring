package com.example.MyMindMate.fcm.infrastructure;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccessTokenProvider {

    @Value("${fcm.file_path}")
    private String serviceAccountPath;

    @Value("${fcm.google_api}")
    private String scope;

    public String getAccessToken() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(serviceAccountPath).getInputStream())
                    .createScoped(List.of(scope));

            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new IllegalStateException("[FCM] 액세스 토큰 발급 실패", e);
        }
    }
}
