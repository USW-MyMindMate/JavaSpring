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

    @Column(name = "ACCOUNT", updatable = false, unique = true)
    private String account;

    @Column(name = "Email")
    private String email;

    @Setter
    @Column(name = "PASSWORD")
    private String password;

    @Setter
    @Column(name = "role")
    private String role;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    //@OneToMany(mappedBy = "parent")
    //private List<User> children;

    @OneToMany(mappedBy = "user")
    private List<ChildProfile> childProfiles;

    // 보호자 단말에 보낼 FCM 토큰
    @Setter
    @Column(name = "fcm_token")
    private String fcmToken;

    @Builder
    public User(String account, String email, String password, String role, String token) {
        this.account = account;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fcmToken = fcmToken;
    }

    public void setParent(User parent) {
        this.parent = parent;
    }

    public Long getParentId() {
        return parent != null ? parent.getId() : null;
    }

    public String getName() {
        return account;
    }
}
