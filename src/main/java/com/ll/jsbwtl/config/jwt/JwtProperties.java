package com.ll.jsbwtl.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// application.yml 파일의 jwt 설정 정보를 읽어오는 클래스
@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret; // JWT 서명에 사용할 비밀 키

    private long accessExp; // access Token 만료 시간

    private long refreshExp; // refresh Token 만료 시간
}
