package com.example.MyMindMate.mood.repository;

import com.example.MyMindMate.mood.Mood;
import com.example.MyMindMate.mood.MoodTypeName;
import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    @Query("SELECT new com.example.MyMindMate.mood.dto.MoodStatsResponse(m.moodTypeName, COUNT(m)) " +
            "FROM Mood m WHERE m.userId = :userId GROUP BY m.moodTypeName")
    List<MoodStatsResponse> countByMoodTypeNameGrouped(Long userId);

    // 감정 반복 감지용 메서드 추가
    long countByUserIdAndMoodTypeName(Long userId, MoodTypeName moodTypeName);

    // 2일 지난 감정 삭제용 메서드 추가
    void deleteByRecordedAtBefore(LocalDateTime dateTime);
}
