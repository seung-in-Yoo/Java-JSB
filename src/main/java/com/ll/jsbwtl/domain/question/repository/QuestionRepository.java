package com.ll.jsbwtl.domain.question.repository;

import com.ll.jsbwtl.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @EntityGraph(attributePaths = {"answers", "answers.author"})
    Optional<Question> findWithAnswersById(Long id);

    @Query("select q from Question q " +
            "where (?1 is null or q.categoryName = ?1) " +
            "and (?2 is null or " +
            "     lower(q.title)      like lower(concat('%', ?2, '%')) or " +
            "     lower(q.content)    like lower(concat('%', ?2, '%')) or " +
            "     lower(q.authorName) like lower(concat('%', ?2, '%')))")
    Page<Question> search(String categoryName, String kw, Pageable pageable);

}
