package com.example.MyMindMate.member.open.service;

import com.example.MyMindMate.email.domain.EmailToken;
import com.example.MyMindMate.email.repository.EmailTokenRepository;
import com.example.MyMindMate.member.domain.ChildProfile;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.dto.ChildLoginDto;
import com.example.MyMindMate.member.dto.ChildProfileDto;
import com.example.MyMindMate.member.dto.UserDto;
import com.example.MyMindMate.member.repository.ChildProfileRepository;
import com.example.MyMindMate.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final ChildProfileRepository childProfileRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    // 계정 존재 여부만 체크
    public User findByAccount(String account) {
        return userRepository.findByAccount(account).orElse(null);
    }

    public void checkAccountDuplicate(String account, String email) {

        EmailToken emailToken = emailTokenRepository.findByEmail(email);

        // 이메일 아직 적지 X -> 아이디 중복 먼저 누를 때
        if(emailToken == null){
            throw new IllegalStateException("이메일 인증 먼저 진행해주세요.");
        }

        // 이메일 인증 링크 누르지 X -> 아이디 중복 누를 때
        if(emailToken.isVerified() == false){
            log.info("이메일 인증 절차를 먼저 처리해주세요");

            throw new IllegalStateException("먼저 이메일로 접속해 인증 링크를 눌러주세요.");
        }

        Optional<User> userOptional = userRepository.findByAccount(account);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("이미 존재하는 회원 아이디: {}", user.getAccount());

            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

    }

    // 비밀번호 입력값과 재입력값 같은지 확인
    public void validatePasswords(String password, String passwordConfirm) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }
        if (!password.equals(passwordConfirm)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        log.info("비밀번호 검증 통과");
    }

    public void signUp(UserDto userDTO, String passwordConfirm){

        EmailToken emailToken = emailTokenRepository.findByEmail(userDTO.getEmail());

        // 이메일 아직 적지 X -> 아이디 중복 먼저 누를 때
        if(emailToken == null){
            throw new IllegalStateException("이메일 인증 먼저 진행해주세요.");
        }

        // 이메일 인증 링크 누르지 X -> 아이디 중복 누를 때
        if(emailToken.isVerified() == false){
            log.info("이메일 인증 절차를 먼저 처리해주세요");

            throw new IllegalStateException("먼저 이메일로 접속해 인증 링크를 눌러주세요.");
        }

        Optional<User> userOptional = userRepository.findByAccount(userDTO.getAccount());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.info("이미 존재하는 회원 아이디: {}", user.getAccount());

            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 검증
        validatePasswords(userDTO.getPassword(), passwordConfirm);

        User user = User.builder()
                .account(userDTO.getAccount())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword())) // 비밀번호 암호화
                .role("PARENT") // 기본 역할 설정
                .build();

        userRepository.save(user);

        log.info("회원가입 완료: {}", user.getAccount());

        // 회원 가입 완료 후 emailtoken 삭제
        emailTokenRepository.delete(emailToken);
        log.info("회원가입 완료 회원 이메일 토큰 삭제:{}, {}", user.getAccount(), user.getEmail());

    }

    // 사용자 정보 저장/업데이트
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User childLogin(String ChildAccount) {
        // 계정으로 유저 조회
        User user = userRepository.findByAccount(ChildAccount)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        // 로그인 시간 저장
        user.setLoginTime(System.currentTimeMillis());
        user.setLogoutTime(null); // 새 로그인 시 logoutTime 초기화

        userRepository.save(user);

        return user;
    }

    @Transactional
    public void childLogout(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        user.setLogoutTime(System.currentTimeMillis());
        userRepository.save(user);
    }

    @Transactional
    public ChildProfile createChildProfile(ChildProfileDto dto) {

        // 부모 account로 부모 User 찾기
        User parentUser = userRepository.findByAccount(dto.getParent_Account())
                .orElseThrow(() -> new IllegalArgumentException("부모 계정을 찾을 수 없습니다."));

        // 자식 User 객체 생성 (나중에 회원으로 승격될 수 있음)
        User childUser = new User();
        childUser.setAccount(dto.getChild_Account());
        childUser.setRole("CHILD");
        childUser.setParent(parentUser); // 핵심 부분 👇 부모와 연결

        userRepository.save(childUser);

        // ChildProfile 엔티티 생성
        ChildProfile profile = new ChildProfile();
        profile.setUser(parentUser);       // 부모 참조
        profile.setAccount(dto.getChild_Account());
        profile.setBirthdate(dto.getBirthdate());

        return childProfileRepository.save(profile);
    }

    public List<String> findChildByParent(String parentAccount) {
        User parent = userRepository.findByAccount(parentAccount)
                .orElseThrow(() -> new IllegalArgumentException("부모 계정을 찾을 수 없습니다: " + parentAccount));

        // 부모가 가진 자식 리스트 (User 테이블의 parent_id 관계 기준)
        return userRepository.findByParent_Id(parent.getId()).stream()
                .map(User::getAccount)
                .collect(Collectors.toList());
    }

    @Transactional
    public void login(UserDto userDto) {

        User user = userRepository.findByAccount(userDto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 로그인 시간 기록
        user.setLoginTime(System.currentTimeMillis());
        user.setLogoutTime(null);

        userRepository.save(user);
    }
}

