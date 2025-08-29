package com.ll.jsbwtl.domain.user.entity;

import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;


    //소셜로그인
    private String provider;   // google, kakao, github, naver

    private String providerId; // 소셜 고유ID
}