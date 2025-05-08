package com.example.MyMindMate.mood;

import jakarta.persistence.*;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activity;

    @ManyToOne
    private MoodType moodType;
}
