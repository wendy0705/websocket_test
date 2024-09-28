package com.example.chattest.config;

import com.example.chattest.handler.ChatWebSocketHandler;
import com.example.chattest.handler.NotificationHandler;
import com.example.chattest.interceptor.CustomHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final NotificationHandler notificationHandler;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler, NotificationHandler notificationHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(chatWebSocketHandler, "/chat")
                .addInterceptors(new CustomHandshakeInterceptor())
                .setAllowedOrigins("*");

        registry.addHandler(notificationHandler, "/notifications")
                .setAllowedOrigins("*");
    }
}

