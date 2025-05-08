package com.example.MyMindMate.mood;

import jakarta.persistence.*;

@Entity
public class MoodRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MoodType moodType;

    @ManyToOne
    private Activity activity;
}
