package com.example.MyMindMate.mood.service;

import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.fcm.service.FcmService;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.mood.Mood;
import com.example.MyMindMate.mood.MoodRecommendation;
import com.example.MyMindMate.mood.MoodTypeName;
import com.example.MyMindMate.mood.dto.MoodRecordRequest;
import com.example.MyMindMate.mood.dto.MoodRecordResponse;
import com.example.MyMindMate.mood.dto.MoodRecommendationDto;
import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import com.example.MyMindMate.mood.repository.MoodRecommendationRepository;
import com.example.MyMindMate.mood.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final MoodRecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    /**
     * 감정 기록 (account → userId 변환 포함)
     */
    @Transactional
    public MoodRecordResponse recordMood(MoodRecordRequest request) {
        // account 기반으로 userId 변환
        User user = userRepository.findByAccount(request.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        Long userId = user.getId();

        MoodTypeName type = MoodTypeName.valueOf(request.getMoodTypeName().toUpperCase());

        // 감정 저장
        moodRepository.save(
                Mood.builder()
                        .userId(userId)
                        .moodTypeName(type)
                        .reason(request.getReason())
                        .recordedAt(LocalDateTime.now())
                        .build()
        );

        boolean isRepeatedNegative = isRepeatedNegativeMood(userId);

        // 보호자 알림
        if (user.getParentId() != null && isRepeatedNegative) {
            notifyParent(userId, type);
        }

        // 3회 연속 부정 감정 시 추천 문구 반환
        String recommendation = null;
        if (isRepeatedNegative) {
            List<String> messages = new ArrayList<>(List.of(
                    "기분이 자주 우울하신가요? 따뜻한 차 한 잔으로 잠시 쉬어가세요 ☕",
                    "힘든 감정이 계속될 땐, 가벼운 산책으로 마음을 환기시켜보세요 🌿",
                    "지금 감정이 반복되고 있네요. 좋아하는 음악을 들으며 기분을 풀어보는 건 어때요? 🎧"
            ));
            Collections.shuffle(messages);
            recommendation = messages.get(0);
        }

        return MoodRecordResponse.builder()
                .message("감정 기록 완료")
                .recommendation(recommendation)
                .build();
    }

    /**
     * 최근 3개의 감정이 모두 부정인지 확인
     */
    public boolean isRepeatedNegativeMood(Long userId) {
        List<Mood> recentMoods = moodRepository.findTop3ByUserIdOrderByRecordedAtDesc(userId);
        return recentMoods.size() >= 3 &&
                recentMoods.stream().allMatch(m -> m.getMoodTypeName() != null && m.getMoodTypeName().isNegative());
    }

    /**
     * 부모에게 FCM 알림 전송
     */
    private void notifyParent(Long childId, MoodTypeName moodTypeName) {
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("자녀 정보를 찾을 수 없습니다."));
        User parent = userRepository.findById(child.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("부모 정보를 찾을 수 없습니다."));

        if (parent.getFcmToken() == null || parent.getFcmToken().isBlank()) return;

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

    /** 감정별 추천 활동 조회 */
    public List<MoodRecommendationDto> getRecommendations(MoodTypeName moodTypeName) {
        return recommendationRepository.findByMoodTypeName(moodTypeName).stream()
                .map(r -> new MoodRecommendationDto(r.getActivity().getActivity()))
                .collect(Collectors.toList());
    }

    /** account 기반 감정 통계 조회 */
    public List<MoodStatsResponse> getMoodStatsByAccount(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        return moodRepository.countByMoodTypeNameGrouped(user.getId());
    }

    /** 2일 지난 감정 자동 삭제 */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldMoods() {
        moodRepository.deleteByRecordedAtBefore(LocalDateTime.now().minusDays(2));
    }
}
