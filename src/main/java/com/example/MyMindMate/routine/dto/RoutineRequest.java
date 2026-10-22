package com.example.MyMindMate.routine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineRequest {
//    private String parentAccount;
    private String childAccount;
    private String title;
    private String time;
    private String dayOfWeek;
}
