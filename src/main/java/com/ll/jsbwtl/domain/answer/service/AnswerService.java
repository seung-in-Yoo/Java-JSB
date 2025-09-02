package com.ll.jsbwtl.domain.answer.service;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.answer.repository.AnswerRepository;
import com.ll.jsbwtl.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    public void create(Question question, String content) {
        Answer answer = new Answer(question, content);
        answerRepository.save(answer);
    }

    public long count() {
        return answerRepository.count();
    }

    public Answer save(Answer a2) {
        return answerRepository.save(a2);
    }
}
