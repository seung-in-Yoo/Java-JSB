package com.ll.jsbwtl.domain.user.controller;

import com.ll.jsbwtl.config.jwt.JwtTokenProvider;
import com.ll.jsbwtl.domain.user.dto.UserLoginRequest;
import com.ll.jsbwtl.domain.user.dto.UserLoginResponse;
import com.ll.jsbwtl.domain.user.dto.UserSignupRequest;
import com.ll.jsbwtl.domain.user.dto.UserSignupResponse;
import com.ll.jsbwtl.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwt;

    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // templates/user/login.html 로 이동
    }

    // 로그인 성공 후 페이지
    @GetMapping("/login/success")
    public String successPage(@RequestParam(required = false) String token, Model model) {
        
        if (token == null || token.isBlank()) {
            return "redirect:/";
        }
        // token은 사용자가 보기보다는 쿠키나 localStorage로 처리해야 안전함
        return "user/login-success";
    }

    // 로그인 처리 (JSON)
    @PostMapping("/user/login")
    @ResponseBody
    public Map<String, String> login(@RequestBody UserLoginRequest req) {
        return userService.login(req.getUsername(), req.getPassword())
                .map(token -> Map.of("accessToken", token))
                .orElse(Map.of("error", "아이디 또는 비밀번호가 잘못되었습니다."));
    }

    
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
}