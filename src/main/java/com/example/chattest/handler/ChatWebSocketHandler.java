package com.example.chattest.handler;

import com.example.chattest.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = "testUser" + Math.random(); // 模擬唯一用戶名，方便測試多人聊天

        session.getAttributes().put("username", username);
        chatService.addUserToChatRoom(username, session); // 將用戶加入聊天室
        System.out.println("用戶 " + username + " 已連線");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 處理訊息，這裡可以加入更多的業務邏輯
        String senderUsername = (String) session.getAttributes().get("username");

        // 廣播訊息給聊天室內的其他成員
        List<WebSocketSession> participants = chatService.getChatRoomParticipants(senderUsername);

        for (WebSocketSession participant : participants) {
            if (participant.isOpen() && !participant.equals(session)) { // 排除自己，發送給其他人
                participant.sendMessage(new TextMessage(senderUsername + ": " + message.getPayload()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("username");
        chatService.removeUserFromChatRoom(username, session);
        System.out.println("用戶 " + username + " 已斷線");// 當用戶斷線時移除session
    }
}

