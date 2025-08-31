package com.ll.jsbwtl.domain.user.service;


import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.domain.user.repository.UserRepository;
import com.ll.jsbwtl.global.oauth.OAuth2Attrs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User upsertOAuthUser(String provider, OAuth2Attrs.Profile p) {
        System.out.println("소셜 로그인 사용자 정보: " + p.name);

        return userRepository.findByProviderAndProviderId(provider, p.providerId)
                .map(u -> {
                    // 필요 시 동기화(이름/이메일 갱신 등)
                    if (p.email != null && !Objects.equals(u.getEmail(), p.email)) u.setEmail(p.email);
                    if (p.name  != null && !Objects.equals(u.getNickname(),  p.name))  u.setNickname(p.name);
                    return u;
                })
                .orElseGet(() -> {
                    User u = new User();
                    u.setProvider(provider);
                    u.setProviderId(p.providerId);
                    u.setEmail(p.email);
                    u.setNickname(p.name);
                    u.setUsername(generateUsername(p)); // 충돌나면 숫자 붙여서 유니크 보장

                    u.setPassword("test");
                    return userRepository.save(u);
                });
    }
    @Transactional
    public User upsertLocalUser(String username, String email, String password, String nickname) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setNickname(nickname);
        u.setPassword(password);   // ⚠ 평문, 나중에 암호화 가능
        u.setProvider("local");
        return userRepository.save(u);
    }
    private String generateUsername(OAuth2Attrs.Profile p) {
        // 선호 규칙: name 있으면 사용, 없으면 providerId 앞부분
        String base = (p.name != null && !p.name.isBlank())
                ? p.name.replaceAll("\\s+", "_").toLowerCase()
                : ("user_" + p.providerId);
        String candidate = base;
        int n = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + "_" + n++;
        }
        return candidate;
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
