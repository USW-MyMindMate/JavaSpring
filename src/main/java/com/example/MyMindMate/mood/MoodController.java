package com.example.MyMindMate.mood;

import com.example.MyMindMate.mood.dto.MoodRecordRequest;
import com.example.MyMindMate.mood.MoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mood")
@RequiredArgsConstructor
public class MoodController {

    private final MoodService moodService;

    @PostMapping("/record")
    public ResponseEntity<String> recordMood(@RequestBody MoodRecordRequest request) {
        moodService.recordMood(request);
        return ResponseEntity.ok("감정 기록 및 FCM 처리 완료");
    }
}
