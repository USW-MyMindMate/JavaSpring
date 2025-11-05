package com.example.MyMindMate.mood.controller;

import com.example.MyMindMate.mood.MoodTypeName;
import com.example.MyMindMate.mood.dto.MoodRecordRequest;
import com.example.MyMindMate.mood.dto.MoodRecordResponse;
import com.example.MyMindMate.mood.dto.MoodRecommendationDto;
import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import com.example.MyMindMate.mood.service.MoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moods")
public class MoodController {

    private final MoodService moodService;

    // 감정 기록 시 활동 추천 문구까지 응답
    @PostMapping
    public ResponseEntity<MoodRecordResponse> recordMood(@RequestBody MoodRecordRequest request) {
        MoodRecordResponse response = moodService.recordMood(request);
        return ResponseEntity.ok(response);
    }

    // 감정별 추천 활동 조회
    @GetMapping("/recommend")
    public ResponseEntity<List<MoodRecommendationDto>> recommend(@RequestParam MoodTypeName moodTypeName) {
        return ResponseEntity.ok(moodService.getRecommendations(moodTypeName));
    }

    // 감정 통계 조회
    @GetMapping("/stats")
    public ResponseEntity<List<MoodStatsResponse>> getStats(@RequestParam Long userId) {
        return ResponseEntity.ok(moodService.getMoodStats(userId));
    }
}
