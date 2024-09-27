package com.example.chattest.handler;

import com.example.chattest.document.ChatMessage;
import com.example.chattest.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String userId = (String) session.getAttributes().get("userId");
        String roomName = (String) session.getAttributes().get("roomName");
//      ex.
//      userId = 1
//      session = StandardWebSocketSession[id=f704709b-092a-e2d0-e6eb-3636443d1e6f, uri=ws://localhost:8080/chat?userId=2&roomName=1_2]
        chatService.addUserToChatRoom(roomName, session);

        List<ChatMessage> previousMessages = chatService.getChatHistory(roomName);

        for (ChatMessage chatMessage : previousMessages) {
            String messageSender = chatMessage.getSenderId() == Integer.parseInt(userId) ? "我" : String.valueOf(chatMessage.getSenderId());
            session.sendMessage(new TextMessage(messageSender + ": " + chatMessage.getMessage()));
        }
        System.out.println("用戶 " + userId + " 已連線");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 處理發送消息
        String roomName = (String) session.getAttributes().get("roomName");
        String userId = (String) session.getAttributes().get("userId");
        String messageContent = message.getPayload();

        // 廣播消息給同一個房間的其他成員
        List<WebSocketSession> participants = chatService.getChatRoomParticipants(roomName);
        for (WebSocketSession participant : participants) {
            if (participant.isOpen() && !participant.equals(session)) {
                participant.sendMessage(new TextMessage(userId + ": " + messageContent));
            }
        }

        chatService.saveChatMessage(roomName, Integer.parseInt(userId), messageContent);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String roomName = (String) session.getAttributes().get("roomName");
        chatService.removeUserFromChatRoom(roomName, userId, session);
    }

}

