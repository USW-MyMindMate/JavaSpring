package com.example.MyMindMate.mood;

import com.example.MyMindMate.fcm.service.FcmService;
import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.mood.Mood;
import com.example.MyMindMate.mood.dto.MoodRecordRequest;
import com.example.MyMindMate.mood.MoodRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    // 감정 기록 (자녀 기능)
    @Transactional
    public void recordMood(MoodRecordRequest request) {
        // 감정 저장
        Mood mood = moodRepository.save(
                Mood.builder()
                        .userId(request.getUserId())
                        .reason(request.getReason())
                        .recordedAt(LocalDateTime.now())
                        .build()
        );

        // 감정 저장 후 반복 감정 여부 검사
        if (isRepeatedNegativeMood(request.getUserId())) {
            notifyParent(request.getUserId(), "부정 감정");
        }
    }

    //최근 감정 3회가 모두 부정 감정인지 검사
    private boolean isRepeatedNegativeMood(Long userId) {
        List<Mood> recentMoods = moodRepository.findTop3ByUserIdOrderByRecordedAtDesc(userId);

        if (recentMoods.size() < 3) return false;

        // 부정 감정 리스트 (임시로 reason 기반)
        List<String> negativeKeywords = List.of("슬픔", "불안", "화남");

        // 최근 3개의 감정이 모두 부정 감정이면 true
        return recentMoods.stream()
                .allMatch(m -> m.getReason() != null &&
                        negativeKeywords.stream().anyMatch(k -> m.getReason().contains(k)));
    }

    //보호자에게 FCM 푸시 알림 전송
    private void notifyParent(Long childId, String moodType) {
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("자녀 정보를 찾을 수 없습니다."));

        User parent = userRepository.findById(child.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("보호자 정보를 찾을 수 없습니다."));

        if (parent.getFcmToken() == null || parent.getFcmToken().isBlank()) {
            System.out.println("[FCM] 보호자의 FCM 토큰이 존재하지 않습니다.");
            return;
        }

        String messageBody = child.getName() + "님이 최근 '" + moodType + "' 감정을 자주 기록하고 있어요 ";

        fcmService.sendNotification(
                MessagePushServiceRequest.builder()
                        .targetToken(parent.getFcmToken())
                        .title("MyMindMate 감정 알림")
                        .body(messageBody)
                        .build()
        );

        System.out.println("[FCM] 보호자에게 감정 알림 전송 완료!");
    }
}
