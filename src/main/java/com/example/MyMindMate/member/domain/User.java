package com.example.MyMindMate.member.domain;

import com.example.MyMindMate.email.domain.EmailToken;
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

    @Setter
    @Column(name = "ACCOUNT", unique = true)
    private String account;

    @Column(name = "Email")
    private String email;

    @Setter
    @Column(name = "PASSWORD")
    private String password;

    @Setter
    @Column(name = "role")
    private String role;

    @Setter
    @Column(name="LoginTime")
    private Long loginTime;

    @Setter
    @Column(name="logoutTime")
    private Long logoutTime;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    @Setter
    @OneToMany(mappedBy = "user")
    private List<ChildProfile> childProfiles;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private EmailToken emailToken;

    // Builder용 생성자
    @Builder
    public User(String account, String email, String password, String role, String token) {
        this.account = account;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
