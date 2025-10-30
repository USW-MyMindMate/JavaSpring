package com.example.MyMindMate.mood.repository;

import com.example.MyMindMate.mood.MoodRecommendation;
import com.example.MyMindMate.mood.MoodTypeName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoodRecommendationRepository extends JpaRepository<MoodRecommendation, Long> {
    List<MoodRecommendation> findByMoodTypeName(MoodTypeName moodTypeName);
}
