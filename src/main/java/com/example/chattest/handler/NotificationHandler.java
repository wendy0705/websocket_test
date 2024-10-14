package com.example.chattest.handler;

import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.example.chattest.manager.WebSocketSessionManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
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

            // 啟動心跳機制，定期發送 PingMessage
            startHeartbeat(session);
        } else {
            System.out.println("Error: userId is not provided in the WebSocket connection URL.");
        }
    }

    // 方法來啟動心跳機制
    private void startHeartbeat(WebSocketSession session) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session.isOpen()) {
                        // 發送 PingMessage 來保持連線
                        session.sendMessage(new PingMessage(ByteBuffer.wrap("ping".getBytes())));
                    } else {
                        // 如果 session 已經關閉，停止心跳機制
                        timer.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    timer.cancel();
                }
            }
        }, 0, 25000); // 每 25 秒發送一次心跳
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
