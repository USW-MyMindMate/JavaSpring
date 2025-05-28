package com.example.MyMindMate.routine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineRequest {
    private Long userId;
    private Long createdBy;
    private String title;
    private String time;
    private String dayOfWeek;

}
