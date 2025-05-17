package com.example.MyMindMate.global.Scheduling;

import com.example.MyMindMate.email.repository.EmailTokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@EnableAsync
public class SchedulingConfig {


}
