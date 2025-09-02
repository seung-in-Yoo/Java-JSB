package com.ll.jsbwtl.domain.answer.entity;

import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


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

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    public Answer(Question question, String content, User author) {
        this.question = question;
        this.content = content;
        this.author = author;
    }

}
