package com.example.MyMindMate.routine.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineResponse {
    private Long id;
    private String childAccount;
    private String title;
    private String time;
    private String dayOfWeek;
}

