package com.example.MyMindMate.mood.controller;

import com.example.MyMindMate.mood.MoodTypeName;
import com.example.MyMindMate.mood.dto.MoodRecordRequest;
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

    @PostMapping
    public ResponseEntity<Void> recordMood(@RequestBody MoodRecordRequest request) {
        moodService.recordMood(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<MoodRecommendationDto>> recommend(@RequestParam MoodTypeName moodTypeName) {
        return ResponseEntity.ok(moodService.getRecommendations(moodTypeName));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<MoodStatsResponse>> getStats(@RequestParam Long userId) {
        return ResponseEntity.ok(moodService.getMoodStats(userId));
    }
}
