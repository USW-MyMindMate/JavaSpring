package com.example.MyMindMate.mood;

import lombok.Getter;

@Getter
public enum MoodTypeName {
    HAPPY, SAD, ANGRY, ANXIOUS, TIRED;

    public boolean isNegative() {
        return this == SAD || this == ANGRY || this == ANXIOUS || this == TIRED;
    }
}

// 감정 수정
