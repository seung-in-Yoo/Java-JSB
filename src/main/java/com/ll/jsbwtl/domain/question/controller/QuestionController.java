package com.ll.jsbwtl.domain.question.controller;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.question.dto.QuestionForm;
import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService service;

    // 목록
    @GetMapping
    public String list(@RequestParam(defaultValue="0") int page,
                       @RequestParam(defaultValue="10") int size,
                       @RequestParam(required=false) String category,
                       @RequestParam(name="q", required=false) String kw,
                       @RequestParam(required=false, defaultValue="") String sort,
                       Authentication auth, Model model) {

        Page<Question> p = service.list(category, kw, page, size, sort);
        model.addAttribute("page", p);
        model.addAttribute("isLoggedIn", isLoggedIn(auth));
        return "question/list";
    }

    // 상세(+조회수)
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Authentication auth, Model model) {
        Question q = service.getDetailAndIncrease(id);
        model.addAttribute("question", q);
        List<Answer> answers = q.getAnswers() != null ? q.getAnswers() : new ArrayList<>();
        model.addAttribute("answers", answers); // answers가 null이 아니도록 항상 리스트로 전달
        boolean owner = isLoggedIn(auth) && Objects.equals(auth.getName(), q.getAuthor().getUsername());
        model.addAttribute("isLoggedIn", isLoggedIn(auth));
        model.addAttribute("isQuestionAuthor", owner);
        return "question/detail";
    }

    // 작성 폼
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("form", new QuestionForm());
        return "question/form";
    }

    // 작성 처리
    @PostMapping
    public String create(@ModelAttribute("form") QuestionForm form, Authentication auth) {
        String username = isLoggedIn(auth) ? auth.getName() : "anonymous";
        String name     = isLoggedIn(auth) ? auth.getName() : "익명";
        service.create(form.getTitle(), form.getContent(), form.getCategory(), username, name);
        return "redirect:/questions";
    }

    // 수정 폼 (조회수 증가 X/본인만 접근)
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,Authentication auth, Model model) {
        Question q = service.getById(id); // 별도로 조회 (조회수 증가 없음)
        requireOwner(auth, q);
        model.addAttribute("form", QuestionForm.from(q));
        return "question/form";
    }

    // 수정 처리
    @PutMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("form") QuestionForm form,
                         Authentication auth) {
        Question q = service.getById(id);
        requireOwner(auth,q);

        service.update(id, form.getTitle(), form.getContent(), form.getCategory());
        return "redirect:/questions/" + id;


    }

    // 삭제
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, Authentication auth) {
        Question q = service.getById(id);
        requireOwner(auth,q);

        service.delete(id);
        return "redirect:/questions";
    }

    private boolean isLoggedIn(Authentication a) {
        return a != null && a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken);
    }
    private void requireOwner(Authentication auth, Question q) {
        if (!isLoggedIn(auth) || !q.getAuthorUsername().equals(auth.getName())) {
            throw new AccessDeniedException("본인만 접근할 수 있습니다.");
        }
    }

}
