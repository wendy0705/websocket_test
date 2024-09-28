package com.example.chattest.manager;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    // 保存 userId 對應的 WebSocketSession
    private final ConcurrentHashMap<Long, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    // 添加新的 WebSocketSession
    public void addSession(Long userId, WebSocketSession session) {
        sessionMap.put(userId, session);
    }

    // 獲取 WebSocketSession
    public WebSocketSession getSession(Long userId) {
        return sessionMap.get(userId);
    }

    // 移除 WebSocketSession
    public void removeSession(Long userId) {
        sessionMap.remove(userId);
    }
}
