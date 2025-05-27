package com.example.MyMindMate.email.repository;

import com.example.MyMindMate.email.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EmailTokenRepository extends JpaRepository<EmailToken, UUID> {

    EmailToken findByTokenuuid(UUID tokenuuid);

    EmailToken findByEmail(String email);

    void deleteByExpirationDateBefore(LocalDateTime localDateTime);

}
