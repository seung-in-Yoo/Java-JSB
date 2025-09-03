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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    //로그인 여부 체크
    private boolean isLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    //본인 확인
    private void requireOwner(Authentication auth, Answer answer) {
        if(!isLoggedIn(auth) || answer.getAuthor() == null
                || !auth.getName().equals(answer.getAuthor().getUsername())) {
            throw new IllegalStateException("본인만 접근할 수 있습니다.");
        }
    }


    //Create
    @PostMapping("{id}/answers")
    public String createAnswer(Model model,
                         @PathVariable("id") Long id,
                         @RequestParam("content") String content,
                         Authentication auth){
        if (!isLoggedIn(auth)) {
            return "redirect:/login"; // 로그인 안 되면 로그인 페이지
        }

        Question question = questionService.getById(id);
        String username = auth.getName(); // 로그인 아이디 가져오기


        answerService.create(question, content, username);

        return String.format("redirect:/question/detail/%s", id);
    }


    //Delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Authentication auth) {
        if(!isLoggedIn(auth)) {
            return "redirect:/login";
        }

        Answer answer = answerService.getById(id);
        requireOwner(auth, answer);

        Long questionId = answerService.delete(id, auth.getName());
        return "redirect:/question/detail/%s".formatted(questionId);
    }

    //Update - get
    @GetMapping("/update/{id}")
    public String edit(@PathVariable("id") Long id,
                       Authentication auth,
                       Model model) {
        Answer answer = answerService.getById(id);
        requireOwner(auth, answer);
        model.addAttribute("answer", answer);
        return "answer/modify";
    }

    //Update - post
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         Authentication auth,
                         @RequestParam("content") String content) {
        Answer answer = answerService.getById(id);
        requireOwner(auth, answer);
        Long questionId = answerService.update(id, content, auth.getName());
        return "redirect:/question/detail/%s".formatted(questionId);
    }



    //답변 정렬 + 페이징
    @GetMapping("/list/{questionId}")
    public String list(@PathVariable("questionId") Long questionId,
                       @RequestParam(value = "sort", defaultValue = "desc") String sortOrder,
                       Model model,
                       @PageableDefault(size = 5, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Question question = questionService.getById(questionId);

        Sort sort = Sort.by("createDate");
        sort = "asc".equals(sortOrder) ? sort.ascending() : sort.descending();

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<Answer> paging = answerService.getList(question, sortedPageable);

        model.addAttribute("question", question);
        model.addAttribute("paging", paging);
        model.addAttribute("sortOrder", sortOrder);
        return "answer/list";
    }




}
