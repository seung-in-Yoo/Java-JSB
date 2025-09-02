package com.ll.jsbwtl.domain.answer.entity;

import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    public Answer(Question question, String content) {
        this.question = question;
        this.content = content;
    }
}

