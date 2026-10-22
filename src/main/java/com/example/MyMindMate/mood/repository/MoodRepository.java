package com.example.MyMindMate.mood.repository;

import com.example.MyMindMate.mood.Mood;
import com.example.MyMindMate.mood.MoodTypeName;
import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    @Query("SELECT new com.example.MyMindMate.mood.dto.MoodStatsResponse(m.moodTypeName, COUNT(m)) " +
            "FROM Mood m WHERE m.userId = :userId GROUP BY m.moodTypeName")
    List<MoodStatsResponse> countByMoodTypeNameGrouped(@Param("userId") Long userId);

    // 여러 자녀의 감정 통계 조회
    @Query("SELECT new com.example.MyMindMate.mood.dto.MoodStatsResponse(m.moodTypeName, COUNT(m)) " +
            "FROM Mood m WHERE m.userId IN :userIds GROUP BY m.moodTypeName")
    List<MoodStatsResponse> countByMoodTypeNameGroupedForUserIds(@Param("userIds") List<Long> userIds);

    long countByUserIdAndMoodTypeName(Long userId, MoodTypeName moodTypeName);

    List<Mood> findTop3ByUserIdOrderByRecordedAtDesc(Long userId);

    Optional<Mood> findTopByUserIdOrderByRecordedAtDesc(Long userId);

    void deleteByRecordedAtBefore(LocalDateTime dateTime);

    // moodhistory
    List<Mood> findByUserIdOrderByRecordedAtDesc(Long userId);
}
