package com.ll.jsbwtl.domain.question.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name="question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", length=255, nullable=false)
    private String title;                // 제목

    @Column(columnDefinition = "TEXT", nullable=false)
    private String content;

    @Column(name="created_at")
    private LocalDateTime createDate;

    @Column(name="category_name",length=50, nullable=false)
    private String categoryName;                // 카테고리

    @Column(name="view_count", nullable=false)
    private long viewCount = 0L;            // 조회수

    @Column(name="author_username", length=100, nullable=false)
    private String authorUsername = "anonymous";
    @Column(name="author_name", length=100, nullable=false)
    private String authorName = "익명";

    public void increaseViewCount(){ this.viewCount++; }

    @Transient public String getTitle(){ return title; }
    @Transient public String getCategoryName(){ return categoryName; }
    @Transient public LocalDateTime getCreatedAt(){ return createDate; }

    @Transient private String excerptHtml, contentHtml;
    @Transient private Integer answerCount;
    @Transient private Author author;

    public Author getAuthor(){
        if (author==null) author = new Author(authorName, authorUsername);
        return author;
    }
    public void setAuthor(Author a){ this.author = a; }
    public static class Author {
        private final String name, username;
        public Author(String name,String username){ this.name=name; this.username=username; }
        public String getName(){ return name; } public String getUsername(){ return username; }
    }
}
