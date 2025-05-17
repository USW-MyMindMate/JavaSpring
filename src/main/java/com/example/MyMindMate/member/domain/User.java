package com.example.MyMindMate.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACCOUNT", nullable = false, updatable = false, unique = true)
    private String account;

    @Setter
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Setter
    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    @Column(name = "fcmToken", nullable = false)
    private String fcmToken;

    @OneToMany(mappedBy = "user")
    private List<ChildProfile> childProfiles;

    // Builder용 생성자
    @Builder
    public User(String account, String password, String role, String fcmToken) {
        this.account = account;
        this.password = password;
        this.role = role;
        this.fcmToken = fcmToken;
    }
}
