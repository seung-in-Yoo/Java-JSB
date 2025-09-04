package com.ll.jsbwtl.domain.comment.controller;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.answer.service.AnswerService;
import com.ll.jsbwtl.domain.comment.entity.Comment;
import com.ll.jsbwtl.domain.comment.service.CommentService;
import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AnswerService answerService;
    private final UserService userService;

    // 댓글 작성
    @PostMapping("/answers/{answerId}/comments")
    public String addComment(
            @PathVariable Long answerId,
            @RequestParam String content,
            Authentication auth
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userService.findByUsername(username);

        Answer answer = answerService.getById(answerId);

        commentService.createComment(answer, user, content);

        return "redirect:/questions/" + answer.getQuestion().getId();
    }

    // 댓글 수정
    @PostMapping("/comments/{commentId}/edit")
    public String editComment(
            @PathVariable Long commentId,
            @RequestParam String content,
            Authentication auth
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        Comment comment = commentService.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 작성자 체크
        if (!comment.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/questions/" + comment.getAnswer().getQuestion().getId();
        }

        commentService.updateComment(comment, content);

        return "redirect:/questions/" + comment.getAnswer().getQuestion().getId();
    }

    // 댓글 삭제
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long commentId,
            Authentication auth
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        Comment comment = commentService.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 작성자 체크
        if (!comment.getAuthor().getUsername().equals(auth.getName())) {
            return "redirect:/questions/" + comment.getAnswer().getQuestion().getId();
        }

        commentService.deleteComment(comment);

        return "redirect:/questions/" + comment.getAnswer().getQuestion().getId();
    }
}
