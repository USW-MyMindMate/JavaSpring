package com.example.MyMindMate.mood.entity;

import com.example.MyMindMate.mood.dto.MoodTypeName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "activities") // enum으로 바꿔
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String activity;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type_name", nullable = false)
    private MoodTypeName moodTypeName;
}
