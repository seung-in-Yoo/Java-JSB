package com.ll.jsbwtl.config.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

// JWT 인증 객체를 표현하는 클래스 (인증된 사용자의 정보를 나타냄)
public class JwtAuthentication extends AbstractAuthenticationToken {

    // JWT에서 추출한 사용자 ID
    private final Long userId;

    public JwtAuthentication(Long userId) {
        super(List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.userId = userId;
        // 인증 완료 상태로 설정
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    // 사용자 고유 ID 반환
    @Override
    public Object getPrincipal() {
        return userId;
    }

    public Long getUserId() {
        return userId;
    }
}
