package com.example.chattest.controller;
//
//import com.example.chattest.document.ChatMessage;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class ChatController {
//
//    @GetMapping("/chat-history/{roomName}")
//    public List<ChatMessage> getChatHistory(@PathVariable String roomName) {
//        // 查詢該房間的聊天記錄
//        return chatMessageRepository.findByRoomName(roomName);
//    }
//}

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    // 動態處理 URL 路徑參數，例如 userId
    @GetMapping("/chatroom")
    public String chatroom(@RequestParam("userId") String userId, Model model) {
        // 這裡可以處理 userId，並將其傳遞給 Thymeleaf 模板
        model.addAttribute("userId", userId);
        return "chatroom";  // 返回 Thymeleaf 模板名稱
    }
}
