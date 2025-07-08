package com.example.MyMindMate.mood;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import com.example.MyMindMate.mood.dto.MoodResponse;
import com.example.MyMindMate.mood.dto.MoodStatsResponse;
import com.example.MyMindMate.mood.dto.MoodTypeName;
import com.example.MyMindMate.mood.entity.Mood;
import com.example.MyMindMate.mood.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final MoodRepository moodRepository;
    private final UserRepository userRepository;

    // 아이 id로 아이의 mood 조회
    public List<MoodResponse> getMood (List<User> childs){



        // 아이들의 ID 추출
        List<Long> childIds = childs.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<Mood> moods = moodRepository.findByUserIdInOrderByRecordedAtDesc(childIds);

        return moods.stream()
                .map(mood -> MoodResponse.builder()
                        .userId(mood.getUser().getId())
                        .account(mood.getUser().getAccount())
                        .moodType(mood.getMoodTypeName().name())
                        .reason(mood.getReason())
                        .recordedAt(mood.getRecordedAt())
                        .build()
                )
                .toList();
    }


    public List<MoodStatsResponse> getMoodCount (List<User> childs){

        // 자녀 ID 리스트 생성
        List<Long> childIds = childs.stream()
                .map(User::getId)
                .toList();

        // 감정 전체 가져오기
        List<Mood> moods = moodRepository.findByUserIdIn(childIds);

        // 사용자 ID 기준으로 그룹화
        Map<Long, List<Mood>> moodsByUser = moods.stream()
                .collect(Collectors.groupingBy(m -> m.getUser().getId()));

        return childs.stream().map(child -> {
            List<Mood> userMoods = moodsByUser.getOrDefault(child.getId(), Collections.emptyList());

            Map<MoodTypeName, Long> moodCountMap = userMoods.stream()
                .collect(Collectors.groupingBy(
                        Mood::getMoodTypeName,
                        Collectors.counting()
                ));

        return MoodStatsResponse.builder()
                .userId(child.getId())
                .account(child.getAccount())
                .moodCountMap(moodCountMap)
                .build();
    }).toList();

    }

}
