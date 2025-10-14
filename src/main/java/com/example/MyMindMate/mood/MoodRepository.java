package com.example.MyMindMate.mood;

import com.example.MyMindMate.mood.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    List<Mood> findTop3ByUserIdOrderByRecordedAtDesc(Long userId);
}
