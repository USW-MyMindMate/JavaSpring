package com.example.MyMindMate.mood.dto;

import com.example.MyMindMate.mood.MoodTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoodRecordRequest {
    //private Long moodTypeId;
    private MoodTypeName moodTypeName;
    private String reason;
    private Long userId;
}
