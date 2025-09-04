package com.ll.jsbwtl.config.jwt;

import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String header = req.getHeader("Authorization");

        // 쿠키에서 Authorization 값 추출
        if (header == null || header.isBlank()) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        header = "Bearer " + cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 토큰 유효성 검사 및 인증 처리
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtProvider.validateToken(token)) {
                Long userId = jwtProvider.getUserId(token);

                // userId로 사용자 조회 → username 가져오기
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("사용자 없음"));

                String username = user.getUsername();

                // 인증 객체에 username 설정
                JwtAuthentication authentication = new JwtAuthentication(username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        } else {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(req, res);
    }
}