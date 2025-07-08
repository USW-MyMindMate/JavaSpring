package com.example.MyMindMate.mood.dto;

import com.example.MyMindMate.routine.dto.RoutineWithLogResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponse {
    private List<MoodResponse> moods;
    private List<RoutineWithLogResponse> routineWithLogResponse;
}
