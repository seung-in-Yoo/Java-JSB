package com.ll.jsbwtl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 개발 초기에는 CSRF 끄고 시작
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청 허용 (로그인 없이 접근 가능)
                );

        // TODO:현재는 개발 초기 단계이므로 모든 페이지에 로그인 없이 접근 가능하도록 설정함
        // TODO:로그인 기능이 완성되면 아래 예시처럼 만든 컨트롤러 경로들만 허용으로 바꿔주기
        /*
            http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/", "/login", "/signup",
                        "/question/**", "/answer/**", "/user/**",  // 페이지의 컨트롤러 URL (바뀔수도 있음)
                        "/css/**", "/js/**", "/images/**"
                    ).permitAll()
                    .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
                );
        */

        return http.build();
    }
}

