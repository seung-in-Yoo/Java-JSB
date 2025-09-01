package com.ll.jsbwtl.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.jsbwtl.config.jwt.JwtTokenProvider;
import com.ll.jsbwtl.global.oauth.OAuth2Attrs;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        // 1. OAuth2User 정보 가져오기
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Long localUserId = ((Number) Objects.requireNonNull(oAuth2User.getAttribute("localUserId"))).longValue();
        String role = oAuth2User.getAuthorities().iterator().next().getAuthority();

        // 2. JWT Access Token 발급
        String accessToken = jwtTokenProvider.generateToken(localUserId, role);

        // 3. JSON 형태로 응답 (쿠키 대신)
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);
        responseBody.put("userId", localUserId);
        responseBody.put("username", oAuth2User.getAttribute("username"));

        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));

    //쿠키 사용 방식
//        Cookie cookie = new Cookie("accessToken", accessToken);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        response.sendRedirect("/question/list");
    }
}

