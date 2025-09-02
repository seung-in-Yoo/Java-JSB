package com.ll.jsbwtl.domain.answer.controller;


import com.ll.jsbwtl.domain.answer.service.AnswerService;
import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerContoller {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String detail(Model model,
                         @PathVariable("id") Long id,
                         @RequestParam("content") String content){
        Question question = questionService.getById(id);

        this.answerService.create(question, content);
        return String.format("redirect:/question/detail/%s", id);

    }


}
