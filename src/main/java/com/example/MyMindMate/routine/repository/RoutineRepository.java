package com.example.MyMindMate.routine.repository;

import com.example.MyMindMate.routine.Routine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUserId(Long userId);
}
