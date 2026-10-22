package com.example.MyMindMate.moodhistory.controller;

import com.example.MyMindMate.moodhistory.dto.MoodHistoryResponse;
import com.example.MyMindMate.moodhistory.service.MoodHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moods/history")
public class MoodHistoryController {

    private final MoodHistoryService moodHistoryService;

    /**
     * account 기반 자녀 감정 기록 타임라인 조회
     * 예: GET /api/moods/history?account=child001
     */
    @GetMapping
    public ResponseEntity<List<MoodHistoryResponse>> getMoodHistory(@RequestParam("account") String account) {
        return ResponseEntity.ok(moodHistoryService.getMoodHistory(account));
    }
}
