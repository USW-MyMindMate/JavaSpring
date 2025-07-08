package com.example.MyMindMate.routine.dto;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RoutineWithLogResponse extends RoutineResponse{
    private boolean isCompleted;

    public RoutineWithLogResponse(Long id, String title, String time, String dayOfWeek, String account, boolean isCompleted) {
        super(id, title, time, dayOfWeek, account);
        this.isCompleted = isCompleted;
    }


}
