package com.example.MyMindMate.config;

import com.example.MyMindMate.member.domain.ChildProfile;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.ChildProfileRepository;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.RoutineLog;
import com.example.MyMindMate.routine.repository.RoutineLogRepository;
import com.example.MyMindMate.routine.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ChildProfileRepository childProfileRepository;
    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;

    @Override
    public void run(String... args) throws Exception {
        // 부모 저장
        User parent = User.builder()
                .account("parentuser")
                .email("parent@naver.com")
                .password("1234")
                .role("PARENT")
                .build();

        User savedParent = userRepository.save(parent);

        // 자녀 저장
        User child = User.builder()
                .account("childuser")
                .email("child@naver.com")
                .password("1234")
                .role("CHILD")
                .build();
        child.setParent(savedParent);
        User savedChild = userRepository.save(child);

        // ChildProfile 저장
        ChildProfile childProfile = new ChildProfile(
                null,
                savedChild,
                "childuser",
                LocalDate.of(2015, 5, 10)
        );
        childProfileRepository.save(childProfile);

        // 루틴 생성
        Routine routine = Routine.builder()
                .userId(savedChild.getId())
                .createBy(savedParent.getId())
                .title("아침 운동")
                .time(LocalTime.of(7, 30))
                .dayOfWeek("MONDAY")
                .build();

        Routine savedRoutine = routineRepository.save(routine);

        // 루틴 로그 생성
        RoutineLog routineLog = RoutineLog.builder()
                .routineId(savedRoutine.getId())
                .userId(savedChild.getId())
                .isCompleted(false)
                .build();

        routineLogRepository.save(routineLog);

        System.out.println("===== DataInit 완료 (ChildProfileRepository 방식) =====");
    }


}
