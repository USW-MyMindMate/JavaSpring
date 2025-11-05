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

    /**
     *  감정 기록 API
     * 예: POST /api/moods
     * Body: { "account": "childuser", "moodTypeName": "ANGRY", "reason": "시험 스트레스" }
     */
    @PostMapping
    public ResponseEntity<MoodRecordResponse> recordMood(@RequestBody MoodRecordRequest request) {
        MoodRecordResponse response = moodService.recordMood(request);
        return ResponseEntity.ok(response);
    }

    /**
     *  감정별 추천 활동 조회
     * 예: GET /api/moods/recommend?moodTypeName=ANGRY
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<MoodRecommendationDto>> recommend(@RequestParam("moodTypeName") MoodTypeName moodTypeName) {
        return ResponseEntity.ok(moodService.getRecommendations(moodTypeName));
    }

    /**
     *  account 기반 감정 통계 조회
     * 예: GET /api/moods/stats?account=childuser
     */
    @GetMapping("/stats")
    public ResponseEntity<List<MoodStatsResponse>> getStats(@RequestParam("account") String account) {
        return ResponseEntity.ok(moodService.getMoodStatsByAccount(account));
    }
}
