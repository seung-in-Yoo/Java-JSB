package com.ll.jsbwtl.domain.user.controller;


import com.ll.jsbwtl.domain.user.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // templates/login.html 로 이동
    }
    @GetMapping("user/register")
    public String registerPage() {
        return "user/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String signup(@RequestBody SignupRequest req) {
        userService.upsertLocalUser(
                req.getEmail(),
                req.getPassword(),
                req.getNickname()
        );
        return "회원가입 성공";
    }

    @Getter
    @Setter
    public static class SignupRequest {
        private String email;
        private String password;
        private String nickname;
    }
}
