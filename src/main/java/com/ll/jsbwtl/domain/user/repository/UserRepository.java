package com.ll.jsbwtl.domain.user.repository;

import com.ll.jsbwtl.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.web.PortMapperDsl;

import java.util.Optional;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
public interface UserRepository extends JpaRepository<User, Long> {


    //소셜 로그인
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndProvider(String email, String provider);
}
