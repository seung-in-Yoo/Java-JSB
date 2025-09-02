package com.ll.jsbwtl.domain.answer.service;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.answer.repository.AnswerRepository;
import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    public void create(Question question, String content, User author) {
        Answer answer = new Answer(question, content, author);
        answerRepository.save(answer);
    }

    public long count() {
        return answerRepository.count();
    }

    public Answer save(Answer a2) {
        return answerRepository.save(a2);
    }

    public Long delete(Long id, String username) {
        Answer answer = getById(id);
        if(answer.getAuthor() == null || !answer.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("권한이 없습니다.");
        }
        answer.softDelete();
        answerRepository.save(answer);
        return answer.getQuestion().getId();
    }

    public Long update(Long id, String content, String username) {
        Answer answer = getById(id);
        if(answer.getAuthor() == null || !answer.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("권한이 없습니다.");
        }
        answer.setContent(content);
        answerRepository.save(answer);
        return answer.getQuestion().getId();
    }

    public Page<Answer> getList(Question question, Pageable sortedPageable) {
        return answerRepository.findByQuestionAndDeletedAtIsNull(question, sortedPageable);
    }

    public Answer getById(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new RuntimeException("답변을 찾을 수 없습니다."));
    }
}
