package com.ll.jsbwtl.domain.answer.dto;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerForm {

    private Long id;
    private String content;


    public static AnswerForm from(Answer answer) {
        AnswerForm form = new AnswerForm();
        form.setId(answer.getId());
        form.setContent(answer.getContent());
        return form;
    }
}
