package com.example.MyMindMate.mood.entity;

import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.mood.dto.MoodTypeName;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type_name", nullable = false)
    private MoodTypeName moodTypeName;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User child;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User parent;

    //@Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}

