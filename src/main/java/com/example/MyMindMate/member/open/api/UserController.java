package com.example.MyMindMate.member.open.api;

import com.example.MyMindMate.global.ApiResponse;

import com.example.MyMindMate.member.dto.UserDto;
import com.example.MyMindMate.member.open.service.UserService;
import com.example.MyMindMate.email.domain.EmailToken;
import com.example.MyMindMate.email.service.EmailTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailTokenService emailTokenService;

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse> ConfirmEmail(@RequestParam String email) throws MessagingException {
        EmailToken emailToken = emailTokenService.createEmailToken(email);
        emailTokenService.createVerifyLink(emailToken);


        //이메일 인증 로직
        return ResponseEntity.ok(new ApiResponse("인증 메일 전송 완료되었습니다. 이메일 확인을 해주세요"));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("uuid") UUID tokenuuid) {
        emailTokenService.verifyToken(tokenuuid);
        return ResponseEntity.ok(new ApiResponse("이메일 인증이 완료되었습니다."));
    }

    @GetMapping("/check-account")
    public ResponseEntity<ApiResponse> checkAccountname(@RequestBody UserDto userDTO){
        userService.checkAccountDuplicate(userDTO.getAccount(), userDTO.getEmail());
        //아이디 중복 확인 로직
        return ResponseEntity.ok(new ApiResponse("사용 가능한 아이디입니다."));
    }

    @PostMapping("/sign-up-finish") // 프론트에서  비번 email Account 받음
    public ResponseEntity<ApiResponse> signup(@RequestBody UserDto userDTO){

        userService.signUp(userDTO);

        return ResponseEntity.ok(new ApiResponse("회원가입 완료되었습니다."));
    }

}
