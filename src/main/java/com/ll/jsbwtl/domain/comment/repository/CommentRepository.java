package com.ll.jsbwtl.domain.comment.repository;

import com.ll.jsbwtl.domain.comment.entity.Comment;
import com.ll.jsbwtl.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAnswerOrderByIdAsc(Answer answer);
}
