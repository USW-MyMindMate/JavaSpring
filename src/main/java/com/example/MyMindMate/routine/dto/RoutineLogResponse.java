package com.example.MyMindMate.routine.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RoutineLogResponse {
    private Long routineId;

    @JsonProperty("isCompleted")
    private boolean isCompleted;
}
