package com.example.MyMindMate.routine.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoutineLogResponse {
    private Long id;
    private Long routineId;
    private Long userId;

    @JsonProperty("isCompleted")
    private boolean isCompleted;
}
