package com.example.MyMindMate.member.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChildProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 부모 회원 (Member) 참조
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 자식의 식별용 account (이걸 나중에 Member로 승격시킬 때 사용)
    @Column(name = "ACCOUNT", nullable = false)
    private String account;

    @Column(name = "BIRTHDATE")
    private LocalDate birthdate;
}