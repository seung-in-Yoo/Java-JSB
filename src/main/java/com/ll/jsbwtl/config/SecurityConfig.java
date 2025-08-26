package com.ll.jsbwtl.config;

import com.ll.jsbwtl.config.jwt.JwtAuthenticationFilter;
import com.ll.jsbwtl.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Spring Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // JWT 사용 시 세션을 사용하지 않기 때문에 STATELESS 설정
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청 URL 별로 인증/인가 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/signup",
                                "/css/**", "/js/**", "/images/**",
                                "/question/**", "/answer/**", "/user/**"
                        ).permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )

                // JWT 인증 필터 등록
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}