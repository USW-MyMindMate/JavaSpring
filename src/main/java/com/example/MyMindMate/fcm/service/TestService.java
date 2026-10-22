package com.example.MyMindMate.fcm.service;

import com.example.MyMindMate.fcm.dto.MessagePushServiceRequest;
import com.example.MyMindMate.member.domain.User;
import com.example.MyMindMate.member.repository.ChildProfileRepository;
import com.example.MyMindMate.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestService {

    private final ChildProfileRepository childProfileRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    /**
     * account 기반 부모에게 FCM 푸시
     */
    public void pushToParentByChildAccount(String childAccount, String title, String body) {
        User child = userRepository.findByAccount(childAccount)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자녀 계정입니다: " + childAccount));

        User parent = child.getParent();
        if (parent == null) {
            throw new IllegalStateException("해당 자녀의 부모 정보가 없습니다. account=" + childAccount);
        }

        sendTo(parent, title, body);
    }

    public void pushToParentByChildUserId(Long childUserId, String title, String body) {
        User child = userRepository.findById(childUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자녀 사용자입니다. id=" + childUserId));

        User parent = child.getParent();
        if (parent == null) {
            throw new IllegalStateException("해당 자녀의 부모 정보가 없습니다. id=" + childUserId);
        }

        sendTo(parent, title, body);
    }

    public void pushToParentByChildProfileId(Long childProfileId, String title, String body) {
        var profile = childProfileRepository.findById(childProfileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자녀 프로필입니다. id=" + childProfileId));

        Long childUserId = profile.getUser().getId();
        if (childUserId == null) {
            throw new IllegalStateException("자녀 프로필에 연결된 사용자 ID가 없습니다. id=" + childProfileId);
        }

        pushToParentByChildUserId(childUserId, title, body);
    }

    private void sendTo(User parent, String title, String body) {
        String token = parent.getFcmToken();
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("부모 사용자에 FCM 토큰이 없습니다. userId=" + parent.getId());
        }

        fcmService.sendNotification(
                MessagePushServiceRequest.builder()
                        .targetToken(token)
                        .title(title)
                        .body(body)
                        .build()
        );
    }
}
