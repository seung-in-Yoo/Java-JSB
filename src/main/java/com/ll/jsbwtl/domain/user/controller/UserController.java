package com.ll.jsbwtl.domain.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

// 예시 코드입니다. (지우시고 자유롭게 개발하셔도 돼요)
@Controller
public class UserController {

    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // templates/login.html 로 이동
    }

    @GetMapping("/login/success")
    public String successPage(@RequestParam String token, Model model) {
        model.addAttribute("accessToken", token);
        return "user/login-success"; // user/login-success.html
    }

}
