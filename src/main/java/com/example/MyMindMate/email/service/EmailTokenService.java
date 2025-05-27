package com.example.MyMindMate.email.service;


import com.example.MyMindMate.config.EmailConfig;
import com.example.MyMindMate.email.domain.EmailToken;
import com.example.MyMindMate.email.repository.EmailTokenRepository;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTokenService {
    private final EmailTokenRepository emailTokenRepository;
    private final JavaMailSender javaMailSender;
    private final EmailConfig emailConfig;
    private static final long EXPIRATION_MINUTES = 5L;
    private final UserRepository userRepository;

    @Async
    public void sendEmail(MimeMessage mimeMessage) {
        javaMailSender.send(mimeMessage);
    }

    // 이메일 토큰 생성 및 저장
    public EmailToken createEmailToken(String email) {

        // 1. 이미 이메일 인증된 사용자 존재 여부 확인
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원의 이메일입니다.");
        }

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        EmailToken emailToken = EmailToken.builder()
                .email(email)
                .expirationDate(expiration)
                .verified(false)
                .expired(false)
                .build();

        emailTokenRepository.save(emailToken);

        return emailToken;
    }


    //이메일 인증 링크 생성
    public boolean createVerifyLink(EmailToken emailToken) throws MessagingException {
        String email = emailToken.getEmail();
        String verifyUrl = emailConfig.getBaseUrl() + "/user/verify?uuid=" + emailToken.getTokenuuid();

        // 이메일 전송을 위한 MimeMessage 생성 및 설정
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("회원가입 이메일 인증");
        helper.setFrom("mymindmate@naver.com");

        String emailContent
                = "<p>이메일 인증을 위해 아래 링크를 클릭해주세요.</p>" +
                "<a href='" + verifyUrl + "'>이메일 확인</a>";
        helper.setText(emailContent, true);
        sendEmail(mimeMessage);
        return true;
    }

    // 인증 확인 처리
    public boolean verifyToken(UUID tokenuuid) {

        EmailToken emailToken = emailTokenRepository.findByTokenuuid(tokenuuid);

        // 만료 여부 검사
        if (emailToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            emailToken.setExpired(true);
            emailTokenRepository.save(emailToken);
            log.info(emailToken + "토큰이 만료되었습니다. 다시 인증을 요청해주세요.");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 인증을 요청해주세요.");
        }

        // 이미 인증된 경우
        if (emailToken.isVerified()) {
            log.info(emailToken + "이미 인증이 완료된 이메일입니다.");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이미 인증이 완료된 이메일입니다.");
        }

        log.info(String.valueOf(emailToken));
        emailToken.setVerified(true);
        emailTokenRepository.save(emailToken);

        return true;
    }

}


