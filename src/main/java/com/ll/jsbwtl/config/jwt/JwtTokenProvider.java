package com.ll.jsbwtl.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// JWT 토큰 생성 및 검증 관련
@Component
public class JwtTokenProvider {
    private final JwtProperties jwtProperties; // JwtProperties 주입받음
    private final Key signingKey; // JWT 서명을 위한 Key

    // jwtProperties에 정의된 secret 값을 기반으로 HMAC-SHA 키 생성
    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    // Access Token 생성 메서드
    public String generateToken(Long userId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 사용자 ID를 subject로 설정
                .claim("role", role) // 사용자 권한 정보를 클레임에 담음
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExp())) // 만료시간
                .signWith(signingKey, SignatureAlgorithm.HS256) // 서명 알고리즘과 키 지정
                .compact(); // 최종 JWT 문자열 반환
    }

    //  Refresh Token 생성 메서드 (이건 추후에 확장할때 쓸거고 우선 access token 관련 구현부터 진행)
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExp()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token); // 파싱 성공 => 유효한 토큰
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 파싱 실패 => 유효하지 않은 토큰
        }
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserId(String token) {
        if (!validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // subject에 저장된 사용자 ID 꺼내오기
        String subject = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject);
    }

    // 토큰 만료 시간 조회
    public Long getTokenExpiry(String token) {
        if (!validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime();
    }
}