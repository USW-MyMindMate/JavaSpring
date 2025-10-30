package com.example.MyMindMate.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineLogRequest {
    private Long routineId;
    private Long userId;

    @JsonProperty("isCompleted")
    private boolean isCompleted;
}

