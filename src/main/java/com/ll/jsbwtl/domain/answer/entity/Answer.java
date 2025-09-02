package com.ll.jsbwtl.domain.answer.entity;

import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Transient
    private Author authorInfo;

    public Answer(Question question, String content, User author) {
        this.question = question;
        this.content = content;
        this.author = author;
    }

    public String getContentHtml() {
        return content.replaceAll("\n", "<br>");
    }

    public Author getAuthorInfo() {
        if (authorInfo == null && author != null) {
            authorInfo = new Author(author.getUsername(), author.getNickname());
        }
        return authorInfo;
    }

    public void setAuthorInfo(Author a) {
        this.authorInfo = a;
    }

    public static class Author {
        private final String username;
        private final String name;

        public Author(String username, String name) {
            this.username = username;
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }
    }
}
