package com.example.MyMindMate.routine;


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
@Table(name = "routine_logs")
public class RoutineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long routineId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean isCompleted;

    public void update(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }//isCompleted의 update 메서드 추가
}
