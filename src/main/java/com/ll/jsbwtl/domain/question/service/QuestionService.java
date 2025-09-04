package com.ll.jsbwtl.domain.question.service;

import com.ll.jsbwtl.domain.answer.entity.Answer;
import com.ll.jsbwtl.domain.question.entity.Question;
import com.ll.jsbwtl.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {


    private static final Set<String> ALLOWED = Set.of(
            "교육·학문","IT/테크","게임","엔터테인먼트·예술","생활","건강",
            "사회·정치","경제","여행","스포츠·레저","쇼핑"
    );

    private final QuestionRepository repo;

    // 목록(검색/정렬/페이지)
    public Page<Question> list(String category, String kw, int page, int size, String sort) {
        Sort s = switch (sort == null ? "" : sort.toLowerCase()) {
            case "views"   -> Sort.by(Sort.Direction.DESC, "viewCount");
            case "answers" -> Sort.by(Sort.Direction.DESC, "createdAt"); // 답변 기능 없을 때는 최신순으로
            default        -> Sort.by(Sort.Direction.DESC, "createdAt");
        };

        Page<Question> p = repo.search(blankToNull(category), blankToNull(kw),
                PageRequest.of(page, size, s));

        // 템플릿 가공 필드
        p.forEach(q -> {
            q.setExcerptHtml(toExcerptHtml(q.getContent(), 160));
            q.setAuthor(new Question.Author(q.getAuthorName(), q.getAuthorUsername()));
            // 답변 기능이 없으므로 일단 0으로 설정
            q.setAnswerCount(0);
        });

        return p;
    }

    // 상세 페이지: 조회수 +1
    @Transactional
    public Question getDetailAndIncrease(Long id) {
        Question q = repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 질문을 찾을 수 없습니다. ID: " + id));
        q.increaseViewCount(); // 더티체킹으로 UPDATE
        q.setContentHtml(toContentHtml(q.getContent()));
        q.setAuthor(new Question.Author(q.getAuthorName(), q.getAuthorUsername()));
        return q;
    }

    @Transactional
    public Question getDetailAndIncreaseWithAnswers(Long id) {
        Question q = repo.findWithAnswersById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 질문을 찾을 수 없습니다. ID: " + id));

        q.increaseViewCount();

        q.setContentHtml(toContentHtml(q.getContent()));
        q.setAuthor(new Question.Author(q.getAuthorName(), q.getAuthorUsername()));

        for (Answer a : q.getAnswers()) {
            a.setAuthorInfo(a.getAuthorInfo());
        }

        return q;
    }


    // 수정 폼 등에서 조회(조회수 증가 없음)
    public Question getById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 질문을 찾을 수 없습니다. ID: " + id));
    }

    // 생성
    @Transactional
    public Question create(String title, String content, String category,
                           String authorUsername, String authorName) {
        // 입력값 검증
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해 주세요.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해 주세요.");
        }

        Question q = new Question();
        q.setTitle(title.trim());
        q.setContent(content.trim());
        q.setCategoryName(requireCategory(category));
        q.setAuthorUsername(defaultIfBlank(authorUsername, "anonymous"));
        q.setAuthorName(defaultIfBlank(authorName, "익명"));
        return repo.save(q);
    }

    // 수정
    @Transactional
    public Question update(Long id, String title, String content, String category) {
        // 입력값 검증
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해 주세요.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해 주세요.");
        }

        Question q = repo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 질문을 찾을 수 없습니다. ID: " + id));
        q.setTitle(title);
        q.setContent(content);
        q.setCategoryName(requireCategory(category));
        return q;
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("해당 질문을 찾을 수 없습니다. ID: " + id);
        }
        repo.deleteById(id);
    }

    private String requireCategory(String c) {
        if (c == null || c.isBlank()) {
            throw new IllegalArgumentException("카테고리를 선택해 주세요.");
        }
        if (!ALLOWED.contains(c)) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + c);
        }
        return c;
    }

    private String toExcerptHtml(String raw, int max) {
        if (raw == null) return "";
        String safe = HtmlUtils.htmlEscape(raw, StandardCharsets.UTF_8.name());
        if (safe.length() > max) safe = safe.substring(0, max) + "…";
        return safe.replace("\n", "<br/>");
    }

    private String toContentHtml(String raw) {
        if (raw == null) return "";
        return HtmlUtils.htmlEscape(raw, StandardCharsets.UTF_8.name()).replace("\n", "<br/>");
    }

    private String blankToNull(String s){
        return (s == null || s.isBlank()) ? null : s;
    }

    private String defaultIfBlank(String s, String d){
        return (s == null || s.isBlank()) ? d : s;
    }
}