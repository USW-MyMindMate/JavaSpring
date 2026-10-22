package com.example.MyMindMate.mood;

import lombok.Getter;

@Getter
public enum MoodTypeName {
    HAPPY, SAD, ANGRY, ANXIOUS, TIRED, SICK;

    public boolean isNegative() {
        return this == SAD || this == ANGRY || this == ANXIOUS || this == TIRED || this == SICK;
    }
}
