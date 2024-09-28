package com.example.chattest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chatinvitation")
    public String chatinvitation() {
        return "chatinvitation";  // 返回 Thymeleaf 模板名稱
    }

}
