package com.example.MyMindMate.mood.service;

import com.example.MyMindMate.mood.Mood;
import com.example.MyMindMate.mood.MoodRecommendation;
import com.example.MyMindMate.mood.MoodTypeName;
import com.example.MyMindMate.mood.dto.MoodRecordRequest;
import com.example.MyMindMate.mood.dto.MoodRecommendationDto;
import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import com.example.MyMindMate.mood.repository.MoodRecommendationRepository;
import com.example.MyMindMate.mood.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final MoodRecommendationRepository recommendationRepository;

    public void recordMood(MoodRecordRequest request) {
        Mood mood = Mood.builder()
                .userId(request.getUserId())
                .reason(request.getReason())
                .moodTypeName(request.getMoodTypeName())
                .recordedAt(LocalDateTime.now())
                .build();
        moodRepository.save(mood);
    }

    public List<MoodRecommendationDto> getRecommendations(MoodTypeName moodTypeName) {
        List<MoodRecommendation> recommendations = recommendationRepository.findByMoodTypeName(moodTypeName);
        return recommendations.stream()
                .map(r -> new MoodRecommendationDto(r.getActivity().getActivity()))
                .collect(Collectors.toList());
    }

    public List<MoodStatsResponse> getMoodStats(Long userId) {
        return moodRepository.countByMoodTypeNameGrouped(userId);
    }

    public boolean isRepeatedNegativeMood(Long userId, MoodTypeName moodTypeName) {
        long count = moodRepository.countByUserIdAndMoodTypeName(userId, moodTypeName);
        return count >= 3 && moodTypeName.isNegative(); // 3번 이상 반복 시 & 부정 감정만 체크
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    @Transactional
    public void deleteOldMoods() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        moodRepository.deleteByRecordedAtBefore(twoDaysAgo);
    }
}
