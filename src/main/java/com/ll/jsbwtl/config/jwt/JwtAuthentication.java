package com.ll.jsbwtl.config.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

// JWT 인증 객체를 표현하는 클래스 (인증된 사용자의 정보를 나타냄)
public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String username;

    public JwtAuthentication(String username) {
        super(List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.username = username;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public String getName() {
        return username;
    }
}