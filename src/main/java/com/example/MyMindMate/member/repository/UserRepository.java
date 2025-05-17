package com.example.MyMindMate.member.repository;

import com.example.MyMindMate.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 계정으로 조회하는 메서드 추가
    Optional<User> findByAccount(String account);
}
