package com.example.MyMindMate.email.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class EmailTokenDto {

    private UUID uuid;
    private String email;
    private boolean verified;
    private boolean expired;
    private LocalDateTime expirationDate;

}
