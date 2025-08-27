package com.ll.jsbwtl.domain.question.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Controller
@RequestMapping("/question")
public class QuestionController {

    @GetMapping("/list")
    public String showList() {
        return "question/list";  // 리디렉션 경로 => templates/question/list.html
    }
}
