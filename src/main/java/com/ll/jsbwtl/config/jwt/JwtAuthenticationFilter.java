package com.ll.jsbwtl.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JWT 인증 필터
// 매 요청마다 실행되는 필터로, Authorization 헤더의 JWT 토큰을 검사함
// 유효한 토큰이면 SecurityContext에 인증 정보를 등록

@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        // Authorization 헤더에서 토큰 추출
        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // "Bearer " 이후의 문자열만 추출

            // 2. 토큰 유효성 검증
            if (jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserId(token); // 토큰에서 사용자 ID 추출

                // 인증 객체 생성 후 SecurityContext에 등록
                JwtAuthentication authentication = new JwtAuthentication(userId);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // 유효하지 않은 토큰일 경우 보안 컨텍스트 초기화
                SecurityContextHolder.clearContext();
            }
        } else {
            // Authorization 헤더가 없거나 Bearer 토큰 형식이 아니면 초기화
            SecurityContextHolder.clearContext();
        }

        // 다음 필터로 요청 전달
        chain.doFilter(req, res);
    }
}
