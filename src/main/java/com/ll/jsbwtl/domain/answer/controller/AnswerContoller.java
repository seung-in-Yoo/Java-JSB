package com.ll.jsbwtl.domain.answer.controller;


import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.answer.service.AnswerService;
import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerContoller {
    private final QuestionService questionService;
    private final AnswerService answerService;

    //Create
    public String detail(Model model,
                         @PathVariable("id") Long id,
                         @RequestParam("content") String content,
                         Principal principal){
        Question question = questionService.getById(id);
        String username = principal.getName(); // 로그인 아이디 가져오기
        //User author = userService.getByUsername(username); // UserService로 DB에서 User 가져오기
        //answerService.create(question, content, author);

        return String.format("redirect:/question/detail/%s", id);
    }


    //Delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        String username = principal.getName();
        Long questionId = answerService.delete(id, username);
        return "redirect:/question/detail/%s".formatted(questionId);
    }

    //Update - get
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Principal principal, Model model) {
        Answer answer = answerService.getById(id);
        // 권한 체크 필요 (예: principal.getName()과 answer의 작성자 비교)
        model.addAttribute("answer", answer);
        return "answer/modify";
    }

    //Update - post
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         Principal principal,
                         @RequestParam("content") String content) {
        String username = principal.getName();
        Long questionId = answerService.update(id, content, username);
        return "redirect:/question/detail/%s".formatted(questionId);
    }



    //답변 정렬 + 페이징
    @GetMapping("/list/{questionId}")
    public String list(@PathVariable("questionId") Long questionId,
                       @RequestParam(value = "sort", defaultValue = "desc") String sortOrder,
                       Model model,
                       @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable){

        Question question = questionService.getById(questionId);

        Sort sort = Sort.by("createDate");
        if("asc".equals(sortOrder)) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Answer> paging = answerService.getList(question, sortedPageable);
        model.addAttribute("question", question);
        model.addAttribute("paging", paging);
        model.addAttribute("sortOrder", sortOrder);
        return "answer/list";
    }

}
