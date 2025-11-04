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

    /**
     * 감정 기록
     */
    @Transactional
    public void recordMood(MoodRecordRequest request) {
        MoodTypeName type = MoodTypeName.valueOf(request.getMoodTypeName().toUpperCase());

        // 감정 저장
        moodRepository.save(
                Mood.builder()
                        .userId(request.getUserId())
                        .moodTypeName(type)
                        .reason(request.getReason())
                        .recordedAt(LocalDateTime.now())
                        .build()
        );

        // 최근 3개 감정 조회
        List<Mood> recentMoods = moodRepository.findTop3ByUserIdOrderByRecordedAtDesc(request.getUserId());
        System.out.println("[DEBUG] 최근 3개 감정: " +
                recentMoods.stream().map(m -> m.getMoodTypeName().name()).toList());
        System.out.println("[DEBUG] 3회 연속 부정 감정 여부: " +
                isRepeatedNegativeMood(request.getUserId()));

        // 자녀 계정만 부모에게 알림 전송
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (user.getParentId() != null && isRepeatedNegativeMood(request.getUserId())) {
            notifyParent(request.getUserId(), type);
        } else {
            System.out.println("[DEBUG] 보호자 계정이거나 parent_id가 없어 알림 전송 안 함.");
        }
    }

    /**
     * 최근 3개의 감정이 모두 부정인지 확인
     */
    public boolean isRepeatedNegativeMood(Long userId) {
        List<Mood> recentMoods = moodRepository.findTop3ByUserIdOrderByRecordedAtDesc(userId);
        return recentMoods.size() >= 3 &&
                recentMoods.stream().allMatch(m ->
                        m.getMoodTypeName() != null && m.getMoodTypeName().isNegative());
    }

    /**
     * 부모에게 FCM 알림 전송
     */
    private void notifyParent(Long childId, MoodTypeName moodTypeName) {
        // 자녀 정보 조회
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("자녀 정보를 찾을 수 없습니다."));

        // 부모 정보 강제 재조회 (Hibernate 캐시 무시)
        User parent = userRepository.findById(child.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("부모 정보를 찾을 수 없습니다."));

        // FCM 토큰 확인
        if (parent.getFcmToken() == null || parent.getFcmToken().isBlank()) {
            System.out.println("[DEBUG] 부모 FCM 토큰이 없습니다. 알림 전송 중단.");
            return;
        }

        System.out.println("[DEBUG] 부모 FCM 토큰 확인: " + parent.getFcmToken());

        // 메시지 생성 및 전송
        String title = "MyMindMate 감정 알림";
        String body = child.getName() + "님이 최근 " + moodTypeName.name() + " 감정을 3회 연속 기록했어요.";

        fcmService.sendNotification(
                MessagePushServiceRequest.builder()
                        .targetToken(parent.getFcmToken())
                        .title(title)
                        .body(body)
                        .build()
        );

        System.out.println("[DEBUG] FCM 알림 전송 완료: " + parent.getFcmToken());
    }

    /**
     * 감정별 추천 활동 조회
     */
    public List<MoodRecommendationDto> getRecommendations(MoodTypeName moodTypeName) {
        List<MoodRecommendation> recommendations = recommendationRepository.findByMoodTypeName(moodTypeName);
        return recommendations.stream()
                .map(r -> new MoodRecommendationDto(r.getActivity().getActivity()))
                .collect(Collectors.toList());
    }

    /**
     * 감정 통계 조회
     */
    public List<MoodStatsResponse> getMoodStats(Long userId) {
        return moodRepository.countByMoodTypeNameGrouped(userId);
    }

    /**
     * 2일 지난 감정 자동 삭제
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldMoods() {
        moodRepository.deleteByRecordedAtBefore(LocalDateTime.now().minusDays(2));
    }
}
