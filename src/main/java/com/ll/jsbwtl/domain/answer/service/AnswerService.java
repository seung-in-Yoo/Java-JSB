package com.ll.jsbwtl.domain.answer.service;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.answer.repository.AnswerRepository;
import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    private void requireOwner(Answer answer, String username) {
        if (answer.getAuthor() == null || !answer.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("권한이 없습니다.");
        }
    }

    public void create(Question question, String content, String username) {
        System.out.println("username: " + username); // 계속 에러 떠서 로그 찍어봄
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("작성자를 찾을 수 없습니다."));
        Answer answer = new Answer(question, content, author);
        answerRepository.save(answer);
    }


    public Long delete(Long id, String username) {
        Answer answer = getById(id);
        requireOwner(answer, username);
        answer.softDelete();
        return answer.getQuestion().getId();
    }

    public Long update(Long id, String content, String username) {
        Answer answer = getById(id);
        requireOwner(answer, username);
        answer.setContent(content);
        return answer.getQuestion().getId();
    }

    public Page<Answer> getList(Question question, Pageable sortedPageable) {
        return answerRepository.findByQuestionAndDeletedAtIsNull(question, sortedPageable);
    }

    public Answer getById(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new RuntimeException("답변을 찾을 수 없습니다."));
    }


}
