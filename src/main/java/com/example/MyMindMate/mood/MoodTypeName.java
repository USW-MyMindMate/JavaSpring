package com.example.MyMindMate.mood;

import lombok.Getter;

@Getter
public enum MoodTypeName {
    HAPPY, SAD, ANGRY, SICK, ANXIOUS;

    public boolean isNegative() {
        return this == SAD || this == ANGRY || this == ANXIOUS;
    }
}

