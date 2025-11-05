package com.example.MyMindMate.routine.repository;

import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.RoutineLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    // List<RoutineLog> findByUserId(Long userId);
    List<RoutineLog> findByRoutineId(Long routineId);

    @Query("SELECT rl FROM RoutineLog rl WHERE rl.routine.user.id = :userId")
    List<RoutineLog> findByUserId(@Param("userId") Long userId);

    Optional<RoutineLog> findByRoutine(Routine routine);

    Optional<RoutineLog> findByRoutineIdAndUserId(Long routineId, Long userId); //true,false 가능하게
}
