package com.ll.jsbwtl.domain.user.entity;

import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "provider"})
        }
)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String provider;

    private String providerId; // 소셜 고유ID

    @Column(nullable = true)
    private String email;

    @Column
    private String nickname;
}
