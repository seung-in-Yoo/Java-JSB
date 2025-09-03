package com.ll.jsbwtl.domain.user.controller;

import com.ll.jsbwtl.config.jwt.JwtTokenProvider;
import com.ll.jsbwtl.domain.user.dto.UserLoginRequest;
import com.ll.jsbwtl.domain.user.dto.UserSignupRequest;
import com.ll.jsbwtl.domain.user.dto.UserSignupResponse;
import com.ll.jsbwtl.domain.user.entity.User;
import com.ll.jsbwtl.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwt;

    // 로그인 페이지 렌더링
    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // templates/user/login.html 로 이동
    }

    // 로그인 성공 후 페이지
    @GetMapping("/login/success")
    public String successPage(@RequestParam(name = "token", required = false) String token, Model model) {
        if (token == null || token.isBlank()) {
            return "redirect:/";
        }

        // model.addAttribute("accessToken", token);

        return "user/login-success";
    }

    // 로그인 처리 (JSON)
    @PostMapping("/user/login")
    @ResponseBody
    public Map<String, String> login(@RequestBody UserLoginRequest req, HttpServletResponse response) {
        return userService.login(req.getUsername(), req.getPassword())
                .map(token -> {
                    Cookie cookie = new Cookie("Authorization", token);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(60 * 60);
                    response.addCookie(cookie);

                    return Map.of("accessToken", token); // JSON 응답 포함
                })
                .orElseGet(() -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return Map.of("error", "아이디 또는 비밀번호가 잘못되었습니다.");
                });
    }

    // 회원가입 폼 페이지
    @GetMapping("/user/register")
    public String registerPage() {
        return "user/register";
    }

    // 회원가입 처리 (JSON)
    @PostMapping("/user/register")
    @ResponseBody
    public UserSignupResponse signup(@RequestBody UserSignupRequest req) {
        if (userService.existsByEmail(req.getEmail())) {
            return new UserSignupResponse("이미 존재하는 이메일입니다.");
        }
        if (userService.existsByUsername(req.getUsername())) {
            return new UserSignupResponse("이미 존재하는 사용자 이름입니다.");
        }

        userService.upsertLocalUser(
                req.getUsername(),
                req.getEmail(),
                req.getPassword(),
                req.getNickname()
        );

        return new UserSignupResponse("회원가입 성공");
    }

    @GetMapping("/user/me")
    public String myPage(Authentication auth, Model model) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user/mypage";
    }

    // 수정 폼
    @GetMapping("/user/me/edit")
    public String editPage(Authentication auth, Model model) {
        String username = auth.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "user/edit";
    }

    // 수정 처리
    @PostMapping("/user/me/edit")
    public String editSubmit(
            Authentication auth,
            @RequestParam String nickname,
            @RequestParam String email
    ) {
        String username = auth.getName();
        userService.updateProfile(username, nickname, email);
        return "redirect:/user/me";
    }
}