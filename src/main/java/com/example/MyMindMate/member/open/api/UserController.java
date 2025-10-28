package com.example.MyMindMate.member.open.api;

import com.example.MyMindMate.global.ApiResponse;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.dto.SignUpRequest;
import com.example.MyMindMate.member.dto.UserDto;
import com.example.MyMindMate.member.open.service.UserService;
import com.example.MyMindMate.email.domain.EmailToken;
import com.example.MyMindMate.email.service.EmailTokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.awt.desktop.AboutHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailTokenService emailTokenService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserDto userDto, HttpServletRequest request) {

        // User 객체 가져오기
        User user = userService.findByAccount(userDto.getAccount());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("로그인 실패: 계정이 존재하지 않습니다."));
        }

        log.info("로그인 시도 회원: {}", user.getAccount());

        // 2. 비밀번호 검증 (BCrypt 사용 가정)
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            log.warn("로그인 실패 - 비밀번호 불일치, 계정: {}", userDto.getAccount());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("로그인 실패: 비밀번호가 일치하지 않습니다."));
        }

        // 3. 기존 세션 무효화 (중복 로그인 방지)
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 30); // 30분
        session.setAttribute("loginUser", user);

        log.info("로그인 성공 - 회원: {}, 세션ID: {}", user.getAccount(), session.getId());

//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("id", user.getId());
//        responseData.put("account", user.getAccount());

        // 3. 세션 ID는 서버가 자동으로 Set-Cookie 헤더에 담아서 응답함
        return ResponseEntity.ok(new ApiResponse("로그인이 완료되었습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok(new ApiResponse("로그아웃 되었습니다."));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse> ConfirmEmail(@RequestParam String email) throws MessagingException {
        EmailToken emailToken = emailTokenService.createEmailToken(email);
        emailTokenService.createVerifyLink(emailToken);

        //이메일 인증 로직
        return ResponseEntity.ok(new ApiResponse("인증 메일 전송 완료되었습니다. 이메일 확인을 해주세요"));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("uuid") UUID tokenuuid) {

        EmailToken emailToken = emailTokenService.verifyToken(tokenuuid);
        return ResponseEntity.ok(new ApiResponse("이메일 인증이 완료되었습니다."));
    }

    //이메일 재인증
    @PostMapping("/reconfirm-email")
    public ResponseEntity<ApiResponse> reconfirmEmail(@RequestParam String email) throws MessagingException {
        EmailToken newToken = emailTokenService.recreateEmailToken(email);
        emailTokenService.createVerifyLink(newToken);

        return ResponseEntity.ok(new ApiResponse("재인증 메일 전송이 완료되었습니다."));
    }

    @GetMapping("/check-account")
    public ResponseEntity<ApiResponse> checkAccountname(@RequestBody UserDto userDTO){
        userService.checkAccountDuplicate(userDTO.getAccount(), userDTO.getEmail());
        //아이디 중복 확인 로직
        return ResponseEntity.ok(new ApiResponse("사용 가능한 아이디입니다."));
    }

    @PostMapping("/sign-up-finish") // 프론트에서  비번 email Account 받음
    public ResponseEntity<ApiResponse> signup(@RequestBody SignUpRequest request){

        userService.signUp(request.toUserDto(), request.getPasswordConfirm());

        return ResponseEntity.ok(new ApiResponse("회원가입 완료되었습니다."));
    }
}
