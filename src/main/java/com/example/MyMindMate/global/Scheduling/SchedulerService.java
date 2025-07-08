package com.example.MyMindMate.global.Scheduling;

import com.example.MyMindMate.email.repository.EmailTokenRepository;
import com.example.MyMindMate.mood.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final EmailTokenRepository emailTokenRepository;
    private final MoodRepository moodRepository;

    // 자정 12시가 되면 현재 시간 기준으로 하루가 지난 데이터 삭제
    @Async
    @Scheduled(cron = "0 0 12 * * *")
    public void autoDelete() {

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        log.info("만료된 이메일 토큰 정리 작업 실행됨: {}", LocalDateTime.now());

        emailTokenRepository.deleteByExpirationDateBefore(oneDayAgo);
    }

    @Async
    @Scheduled(cron = "0 0 12 * * *")
    public void moodDelete(){

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        log.info("2일 전 감정 삭제: {}", LocalDateTime.now());

        moodRepository.deleteByRecordedAtBefore(oneDayAgo);
    }

}
