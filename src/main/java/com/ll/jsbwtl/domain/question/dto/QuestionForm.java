package com.ll.jsbwtl.domain.question.dto;

import com.ll.jsbwtl.domain.question.entity.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
    private Long id;
    private String title;
    private String content;
    private String category;

    public static QuestionForm from(Question q){
        QuestionForm f = new QuestionForm();
        f.setId(q.getId());
        f.setTitle(q.getTitle());
        f.setContent(q.getContent());
        f.setCategory(q.getCategoryName());
        return f;
    }
}