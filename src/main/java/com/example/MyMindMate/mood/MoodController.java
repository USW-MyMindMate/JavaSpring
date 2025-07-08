package com.example.MyMindMate.mood;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.dto.UserDto;
import com.example.MyMindMate.member.open.service.UserService;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.mood.dto.HomeResponse;
import com.example.MyMindMate.mood.dto.MoodResponse;
import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import com.example.MyMindMate.mood.entity.Mood;
import com.example.MyMindMate.routine.RoutineService;
import com.example.MyMindMate.routine.dto.RoutineWithLogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/home")
@RestController
@RequiredArgsConstructor
public class MoodController {

    private final UserService userService;
    private final MoodService moodService;
    private final RoutineService routineService;
    private final UserRepository userRepository;

    // Home: 자녀 감정, routine 완료 여부 data 조회
    @PostMapping
    public ResponseEntity<HomeResponse> getMoods(@RequestBody UserDto parentDto) {

        // 1. 부모 유저 엔티티 조회
        User parent = userRepository.findById(parentDto.getId())
                .orElseThrow(() -> new RuntimeException("부모 계정을 찾을 수 없습니다."));

        // 2. 자녀 리스트 조회
        List<User> children = parent.getChildren();

        // 여기서 각 자녀의 mood 정보 조회하거나 가공 가능
        List<Mood> allMoods = new ArrayList<>();
        for (User child : children) {
            allMoods.addAll(child.getMoodsRecorded());
        }

        // 2. 자녀들의 감정 정보 조회
        List<MoodResponse> moodResponseList = moodService.getMood(childs);

        List<RoutineWithLogResponse> routineWithLogresponses = routineService.getRoutineAndRoutineLog(childs); // 응답에 routine 답기

        //응답에 완료여부 포함해서 담기

        HomeResponse homeResponse = new HomeResponse(moodResponseList, routineWithLogresponses);
        return ResponseEntity.ok(homeResponse);
    }

    @PostMapping("/moodgraph")
    public ResponseEntity<List<MoodStatsResponse>> getMoodCounts(UserDto parentDto){

        // 1. 부모 ID를 통해 자녀 유저들 조회
        List<User> childs = userService.getChildsByParentId(parentDto.getParentId());
        for (User child : childs) {
            log.info("자식 ID: {}, 계정: {}", child.getId(), child.getAccount());
        }
        List<MoodStatsResponse> moodStatsResponseList = moodService.getMoodCount(childs);
        for (MoodStatsResponse response : moodStatsResponseList) {
            log.info("자식 ID: {}, 계정: {}, 감정 개수: {}",
                    response.getUserId(),
                    response.getAccount(),
                    response.getMoodCountMap().size());

            response.getMoodCountMap().forEach((moodType, count) -> {
                log.info("  감정 타입: {}, 개수: {}", moodType, count);
            });
        }
        return ResponseEntity.ok(moodStatsResponseList);
    }

}
