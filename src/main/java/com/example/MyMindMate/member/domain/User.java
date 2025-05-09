package com.example.MyMindMate.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ACCOUNT", nullable = false, updatable = false, unique = true)
    private String account;

    @Setter
    @Column(name="PASSWORD", nullable = false)
    private String password;

    @Setter
    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;  // 부모 User와의 관계

    @Column(name = "fcmToken", nullable = false)
    private String fcmToken;

    @OneToMany(mappedBy = "user")
    private List<ChildProfile> childProfiles;  // 자녀 프로필과의 관계


}

