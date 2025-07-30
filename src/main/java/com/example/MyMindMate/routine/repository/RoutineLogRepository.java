package com.example.MyMindMate.routine.repository;

import com.example.MyMindMate.routine.RoutineLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    List<RoutineLog> findByUserId(Long userId);
    List<RoutineLog> findByRoutineId(Long routineId);

    Optional<RoutineLog> findByRoutineIdAndUserId(Long routineId, Long userId); //true,false 가능하게
}
