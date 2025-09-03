package com.ll.jsbwtl.domain.user.service;

import com.ll.jsbwtl.config.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Long localUserId = ((Number) oAuth2User.getAttribute("localUserId")).longValue();
        String role = oAuth2User.getAuthorities().iterator().next().getAuthority();


        // JWT 발급
        String accessToken = jwtTokenProvider.generateToken(localUserId, role);

        response.sendRedirect("/login/success?token=" + accessToken);
    }
}


