package com.example.MyMindMate.mood.repository;

import com.example.MyMindMate.mood.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MoodRepository extends JpaRepository<Mood, Long> {

    List<Mood> findByUserIdInOrderByRecordedAtDesc(List<Long> userId);

    void deleteByRecordedAtBefore(LocalDateTime localDateTime);

    List<Mood> findByUserIdIn(List<Long> userId);

}
