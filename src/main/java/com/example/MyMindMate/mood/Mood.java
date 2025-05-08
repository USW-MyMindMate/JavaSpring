package com.example.MyMindMate.mood;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MoodType moodType;

    private Long userId;
    private String reason;
    private LocalDateTime recordedAt;
}

