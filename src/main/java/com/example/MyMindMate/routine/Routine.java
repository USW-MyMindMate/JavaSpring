package com.example.MyMindMate.routine;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "routines")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "created_by", nullable = false)
    private Long createBy;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalTime time;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;
}
