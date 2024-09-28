package com.example.chattest.handler;

import java.util.concurrent.ConcurrentHashMap;

import com.example.chattest.manager.WebSocketSessionManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

@AllArgsConstructor
@Component
public class NotificationHandler extends TextWebSocketHandler{

    // 用來儲存 WebSocket 連接的會話，key 是 userId，value 是 WebSocketSession
    private final ConcurrentHashMap<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final WebSocketSessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 從連接的 URL 中提取 userId 參數
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            String userIdParam = query.split("userId=")[1];
            Long userId = Long.parseLong(userIdParam);

            sessionManager.addSession(userId, session);  // 存儲 WebSocketSession
            System.out.println("User " + userId + " connected.");
        } else {
            System.out.println("Error: userId is not provided in the WebSocket connection URL.");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String query = session.getUri().getQuery();
        if (query != null && query.contains("userId=")) {
            String userIdParam = query.split("userId=")[1];
            Long userId = Long.parseLong(userIdParam);

            sessionManager.removeSession(userId);  // 移除 WebSocketSession
            System.out.println("User " + userId + " disconnected.");
        }
    }
}
