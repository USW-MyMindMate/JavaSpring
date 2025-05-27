package com.example.MyMindMate.member.open.service;

import com.example.MyMindMate.email.domain.EmailToken;
import com.example.MyMindMate.email.repository.EmailTokenRepository;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.dto.UserDto;
import com.example.MyMindMate.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;

    public void checkAccountDuplicate(String account, String email) {

        EmailToken emailToken = emailTokenRepository.findByEmail(email);

        // 이메일 아직 적지 X -> 아이디 중복 먼저 누를 때
        if(emailToken == null){
            throw new IllegalStateException("이메일 인증 토큰이 존재하지 않습니다. 이메일을 먼저 적어주세요");
        }

        // 이메일 인증 링크 누르지 X -> 아이디 중복 누를 때
        if(emailToken.isVerified() == false){
            log.info("이메일 인증 절차를 먼저 처리해주세요");

            throw new IllegalStateException("먼저 이메일로 접속해 인증 링크를 눌러주세요.");
        }

        Optional<User> userOptional = userRepository.findByAccount(account);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("이미 존재하는 회원 이메일: {}", user.getEmail());

            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
    }

    public void signUp(UserDto userDTO){

        EmailToken emailToken = emailTokenRepository.findByEmail(userDTO.getEmail());
        // 이메일 아직 적지 X -> 아이디 중복 먼저 누를 때
        if(emailToken == null){
            throw new IllegalStateException("이메일 인증 토큰이 존재하지 않습니다. 이메일을 먼저 적어주세요");
        }

        if(userDTO.getAccount() == null){
            throw new IllegalStateException("아이디를 입력해주세요.");
        }



        if(emailToken.isVerified() == false){
            log.info("이메일 인증 먼저 해주세요");

            throw new IllegalStateException("이메일 인증을 먼저 해주세요.");
        }

        User user = User.builder()
                .account(userDTO.getAccount())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword()) // 실제 운영 환경에서는 암호화 필수!
                .role("PARENT") // 기본 역할 설정
                .build();

        userRepository.save(user);

        log.info("회원가입 완료: {}", user.getAccount());

        // 회원 가입 완료 후 emailtoken 삭제
        emailTokenRepository.delete(emailToken);
        log.info("회원가입 완료 회원 이메일 토큰 삭제:{}, {}", user.getAccount(), user.getEmail());

    }

}
