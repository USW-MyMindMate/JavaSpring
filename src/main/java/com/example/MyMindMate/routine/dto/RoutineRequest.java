package com.example.MyMindMate.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineRequest {
    private Long userId;
    private Long createdBy;
    private String title;
    private String time;
    private String dayOfWeek;

}
