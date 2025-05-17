package com.example.MyMindMate.email.domain;


import com.example.MyMindMate.member.domain.User;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "email_table")
public class EmailToken {

    @Id
    @GeneratedValue
    @UuidGenerator  // ✅ 최신 Hibernate 방식
    @Column(length = 36)
    private UUID tokenuuid; // ✅ String이 아니라 UUID 타입으로!


    private LocalDateTime expirationDate ;

    @Setter
    private boolean verified; //✅ 인증 여부

    @Setter
    private boolean expired;

    @Column(nullable = false)
    private String email; // ✅ 이메일만 저장

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    //필드 갱신
    public void updateExpiredToken() {

    }


}
