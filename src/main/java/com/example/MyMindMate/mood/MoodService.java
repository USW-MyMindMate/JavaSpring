package com.example.MyMindMate.mood;

import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.fcm.service.FcmService;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.mood.dto.MoodRecordRequest;
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
        // 1️⃣ 감정 저장
        Mood mood = moodRepository.save(
                Mood.builder()
                        .userId(request.getUserId())
                        .moodTypeName(MoodTypeName.valueOf(request.getMoodTypeName().toUpperCase()))
                        .reason(request.getReason())
                        .recordedAt(LocalDateTime.now())
                        .build()
        );

        // 2️⃣ 최근 3개의 감정이 모두 부정 감정인지 검사
        if (isRepeatedNegativeMood(request.getUserId())) {
            notifyParent(request.getUserId(), mood.getMoodTypeName());
        }
    }

    // 최근 감정 3회가 모두 부정 감정인지 검사
    private boolean isRepeatedNegativeMood(Long userId) {
        List<Mood> recentMoods = moodRepository.findTop3ByUserIdOrderByRecordedAtDesc(userId);
        if (recentMoods.size() < 3) return false;

        // 최근 감정 3개가 모두 부정 감정이면 true
        return recentMoods.stream()
                .allMatch(m -> m.getMoodTypeName() != null && m.getMoodTypeName().isNegative());
    }

    // 보호자에게 FCM 푸시 알림 전송
    private void notifyParent(Long childId, MoodTypeName moodTypeName) {
        User child = userRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("자녀 정보를 찾을 수 없습니다."));

        User parent = child.getParent();
        if (parent == null) {
            System.out.println("[FCM] 부모 정보가 없습니다.");
            return;
        }

        if (parent.getFcmToken() == null || parent.getFcmToken().isBlank()) {
            System.out.println("[FCM] 보호자의 FCM 토큰이 존재하지 않습니다.");
            return;
        }

        // 실제 알림 메시지 구성
        String title = "MyMindMate 감정 알림";
        String body = child.getName() + "님이 최근 " + moodTypeName.name() + " 감정을 3회 연속 기록했어요.";

        fcmService.sendNotification(
                MessagePushServiceRequest.builder()
                        .targetToken(parent.getFcmToken())
                        .title(title)
                        .body(body)
                        .build()
        );

        System.out.println("[FCM] 보호자에게 감정 알림 전송 완료!");
    }
}
