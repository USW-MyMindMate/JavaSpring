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

//    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private EmailToken emailToken;

    // 보호자 단말에 보낼 FCM 토큰
    @Setter
    @Column(name = "fcm_token")
    private String fcmToken;

    // Builder용 생성자
    @Builder
    public User(String account, String email, String password, String role, String token) {
        this.account = account;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fcmToken = fcmToken;
    }

    //datainit으로 인헤 추가
    public void setParent(User parent) {
        this.parent = parent;
    }

    // FCM 로직에서 필요한 헬퍼 메서드 추가
    public Long getParentId() {
        return parent != null ? parent.getId() : null;
    }

    public String getName() {
        return account; // 이름 컬럼이 없다면 account를 이름 대용으로 사용
    }
}
