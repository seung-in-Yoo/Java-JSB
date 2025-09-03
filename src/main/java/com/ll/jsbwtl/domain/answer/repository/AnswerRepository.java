package com.ll.jsbwtl.domain.answer.repository;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Page<Answer> findByQuestionAndDeletedAtIsNull(Question question, Pageable sortedPageable);
}
