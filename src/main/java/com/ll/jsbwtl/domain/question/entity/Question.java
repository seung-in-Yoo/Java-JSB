package com.ll.jsbwtl.domain.question.entity;

import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
    private String content;
}
