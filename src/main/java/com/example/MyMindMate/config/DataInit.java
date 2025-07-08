 package com.example.MyMindMate.config;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.mood.dto.MoodTypeName;
import com.example.MyMindMate.mood.entity.Mood;
import com.example.MyMindMate.mood.repository.MoodRepository;
import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

 @Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MoodRepository moodRepository;
    private final RoutineRepository routineRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. 부모 유저 생성
        User parent1 = User.builder()
                .account("parent1")
                .password("password123")
                .role("PARENT")
                .build();
        userRepository.save(parent1);

        // 2. 자녀 유저 생성
        User child1 = User.builder()
                .account("child1")
                .password("password123")
                .role("CHILD")
                .parent(parent1)  // 또는 parent_id 설정 방식
                .build();
        userRepository.save(child1);

        // 3. 감정(Mood) 데이터 생성
        Mood mood = Mood.builder()
                .moodTypeName(MoodTypeName.HAPPPY)
                .child(child1)
                .parent(parent1)
                .reason("오늘 좋은 하루였어요!")
                .recordedAt(LocalDateTime.now())
                .build();
        moodRepository.save(mood);

        // 4. 루틴(Routine) 생성
        Routine routine1 = Routine.builder()
                .child(child1)          // 자녀로 할당
                .parent(parent1)        // 생성한 부모
                .title("아침 일과")
                .dayOfWeek("MONDAY")
                .isCompleted(true)
                .build();

        routineRepository.save(routine1);

        // 5. 루틴 로그(RoutineLog) 생성
//        RoutineLog routineLog = RoutineLog.builder()
        //.routine(routine1)
        //        .user(child)
        //        .isCompleted(true)
        //        .build();
        //routineLogRepository.save(routineLog);
    }
}

// DataInit : 서버가 시작될 때, 테스트용 데이터를 DB에 자동으로 저장하는 역할