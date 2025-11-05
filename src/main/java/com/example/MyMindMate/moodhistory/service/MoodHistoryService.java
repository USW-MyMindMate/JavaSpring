package com.example.MyMindMate.moodhistory.service;

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

    public List<MoodHistoryResponse> getMoodHistory(Long userId) {
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
