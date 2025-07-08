package com.example.MyMindMate.member.repository;

import com.example.MyMindMate.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 계정으로 조회하는 메서드 추가
    Optional<User> findByAccount(String account);

    Optional<User> findByEmail(String email);

    // 1. 자식 User 객체 리스트
    List<User> findByParentId(Long parentId);

    // 2. 자식들의 ID만 추출
    List<Long> findChildIdsByParentId(Long parentId);
}
