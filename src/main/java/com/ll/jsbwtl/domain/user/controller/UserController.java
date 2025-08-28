package com.ll.jsbwtl.domain.user.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Controller
public class UserController {

    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // templates/login.html 로 이동
    }
}
