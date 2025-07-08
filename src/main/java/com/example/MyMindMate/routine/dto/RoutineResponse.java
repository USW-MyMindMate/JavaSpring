package com.example.MyMindMate.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoutineResponse {
    private Long id;
    private String title;
    private String time;
    private String dayOfWeek;
    private String account;

}
