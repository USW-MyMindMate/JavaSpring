package com.example.MyMindMate.member.repository;

import com.example.MyMindMate.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자 계정으로 조회하는 메서드 추가
    Optional<User> findByAccount(String account);

    Optional<User> findByEmail(String email);

    // 자녀 ID로 부모 엔티티 조회 (parent 정보 + fcmToken 가져오기)
    @Query("SELECT p FROM User c JOIN c.parent p WHERE c.id = :childId")
    Optional<User> findParentByChildId(@Param("childId") Long childId);
}
