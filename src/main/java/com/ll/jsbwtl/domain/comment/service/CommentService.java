package com.ll.jsbwtl.domain.comment.service;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.comment.entity.Comment;
import com.ll.jsbwtl.domain.comment.repository.CommentRepository;
import com.ll.jsbwtl.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 작성
    public Comment createComment(Answer answer, User author, String content) {
        Comment comment = new Comment(answer, author, content);
        return commentRepository.save(comment);
    }

    // 댓글 조회
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    // 댓글 수정
    public Comment updateComment(Comment comment, String newContent) {
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
