package com.example.MyMindMate.config;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .account("testuser")
                .password("1234")
                .role("CHILD")
                .fcmToken("abc123fcm")
                .build();

        userRepository.save(user);
    }
}

// DataInit : 서버가 시작될 때, 테스트용 데이터를 DB에 자동으로 저장하는 역할