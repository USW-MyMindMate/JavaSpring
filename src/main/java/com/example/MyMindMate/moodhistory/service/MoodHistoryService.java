package com.example.MyMindMate.moodhistory.service;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.mood.Mood;
import com.example.MyMindMate.mood.repository.MoodRepository;
import com.example.MyMindMate.moodhistory.dto.MoodHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodHistoryService {

    private final MoodRepository moodRepository;
    private final UserRepository userRepository;

    /**
     * account 기반 감정 기록 조회
     */
    public List<MoodHistoryResponse> getMoodHistory(String account) {
        // account로 userId 변환
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
        Long userId = user.getId();

        // userId 기반 감정 기록 조회
        List<Mood> moods = moodRepository.findByUserIdOrderByRecordedAtDesc(userId);

        return moods.stream()
                .map(mood -> MoodHistoryResponse.builder()
                        .recordedAt(mood.getRecordedAt())
                        .moodTypeName(mood.getMoodTypeName().name())
                        .reason(mood.getReason())
                        .build())
                .collect(Collectors.toList());
    }
}
