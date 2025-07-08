package com.example.MyMindMate.member.domain;

import com.example.MyMindMate.mood.entity.Mood;
import com.example.MyMindMate.routine.Routine;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "user")
    private List<ChildProfile> childProfiles;

    // 🔸 자식(아이) 계정들이 이 유저를 parent로 참조함
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<User> children;

    // ✅ 이 유저가 부모일 경우, 만든 루틴들
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Routine> createdRoutines;

    // ✅ 이 유저가 자녀일 경우, 자신에게 할당된 루틴들
    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE)
    private List<Routine> assignedRoutines;

    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE)
    private List<Mood> moodsRecorded = new ArrayList<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Mood> moodsCreated = new ArrayList<>();





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
