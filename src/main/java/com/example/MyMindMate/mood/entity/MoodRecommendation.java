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
@Table(name = "mood_recommendations")
public class MoodRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type_name")
    private MoodTypeName moodTypeName;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
}
