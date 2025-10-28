package com.example.MyMindMate.mood;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "moods")
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne
    //private MoodType moodType;

    @Column(nullable = false)
    private Long userId;


    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type_name", nullable = false)
    private MoodTypeName moodTypeName;


    private String reason;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    //추가
    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type_name", nullable = false)
    private MoodTypeName moodTypeName;
}

