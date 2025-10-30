package com.example.MyMindMate.mood.service;

import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.fcm.service.FcmService;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final FcmService fcmService;

    @Transactional
    public void recordMood(MoodRecordRequest request) {
        MoodTypeName type = MoodTypeName.valueOf(request.getMoodTypeName().toUpperCase());

        Mood mood = moodRepository.save(
                Mood.builder()
                        .userId(request.getUserId())
                        .moodTypeName(type)
                        .reason(request.getReason())
                        .recordedAt(LocalDateTime.now())
                        .build()
        );

        if (isRepeatedNegativeMood(request.getUserId())) {
            notifyParent(request.getUserId(), type);
        }
    }

    private boolean isRepeatedNegativeMood(Long userId) {
        List<Mood> recentMoods = moodRepository.findTop3ByUserIdOrderByRecordedAtDesc(userId);
        return recentMoods.size() >= 3 &&
                recentMoods.stream().allMatch(m -> m.getMoodTypeName() != null && m.getMoodTypeName().isNegative());
    }

    private void notifyParent(Long childId, MoodTypeName moodTypeName) {
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("자녀 정보를 찾을 수 없습니다."));

        User parent = child.getParent();
        if (parent == null || parent.getFcmToken() == null || parent.getFcmToken().isBlank()) return;

        String title = "MyMindMate 감정 알림";
        String body = child.getName() + "님이 최근 " + moodTypeName.name() + " 감정을 3회 연속 기록했어요.";

        fcmService.sendNotification(
                MessagePushServiceRequest.builder()
                        .targetToken(parent.getFcmToken())
                        .title(title)
                        .body(body)
                        .build()
        );
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

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldMoods() {
        moodRepository.deleteByRecordedAtBefore(LocalDateTime.now().minusDays(2));
    }
}
