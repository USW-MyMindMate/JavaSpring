package com.example.MyMindMate.routine.repository;

import com.example.MyMindMate.routine.Routine;
import com.example.MyMindMate.routine.dto.RoutineWithLogResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<RoutineWithLogResponse> findRoutineWithLogByUserIdIn(List<Long> userId);


}
