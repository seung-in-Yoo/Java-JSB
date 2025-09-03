package com.ll.jsbwtl.domain.question.entity;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="question")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", length=255, nullable=false)
    private String title; // 제목

    @Column(columnDefinition = "TEXT", nullable=false)
    private String content;

    @Column(name="category_name", length=50, nullable=false)
    private String categoryName; // 카테고리

    @Column(name="view_count", nullable=false)
    private long viewCount = 0L; // 조회수

    @Column(name="author_username", length=100, nullable=false)
    private String authorUsername = "anonymous";

    @Column(name="author_name", length=100, nullable=false)
    private String authorName = "익명";

  
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")  // 최신순 정렬
    private List<Answer> answers = new ArrayList<>();

    public void increaseViewCount() {
        this.viewCount++;
    }

    @Transient
    private String excerptHtml, contentHtml;

    @Transient
    private Integer answerCount;

    @Transient
    private Author author;

    public Author getAuthor() {
        if (author == null) author = new Author(authorName, authorUsername);
        return author;
    }

    public void setAuthor(Author a) {
        this.author = a;
    }

    public static class Author {
        private final String name, username;

        public Author(String name, String username) {
            this.name = name;
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }
    }
}