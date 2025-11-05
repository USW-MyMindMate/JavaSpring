package com.example.MyMindMate.routine;

import com.example.MyMindMate.member.domain.User;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;

    // User 엔티티와 다대일 관계 (루틴 여러 개가 한 명의 사용자에게 속함)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @Column(name = "created_by", nullable = false)
//    private Long createBy;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private LocalTime time;

    @Setter
    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

}
