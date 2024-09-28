package com.example.chattest.service;

import com.example.chattest.document.ChatMessage;
import com.example.chattest.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    // 存放聊天室中的用戶，這裡我們以 Map<String, List<WebSocketSession>> 模擬多個聊天室
    private final Map<String, List<WebSocketSession>> chatRooms = new HashMap<>();

    // 將用戶加入聊天室
    public void addUserToChatRoom(String roomName, WebSocketSession session) {
//        findOrCreateChatRoom(roomName);

        chatRooms.putIfAbsent(roomName, new ArrayList<>()); // 如果聊天室不存在則創建
        List<WebSocketSession> participants = chatRooms.get(roomName);

        if (!participants.contains(session)) {
            participants.add(session);  // 如果用戶尚未加入，則加入房間
        }
        log.info("用戶 {} 加入了房間 {}", session.getAttributes().get("userId"), roomName);
    }

    // 取得聊天室內的成員（除了自己以外的成員）
    public List<WebSocketSession> getChatRoomParticipants(String roomName) {
        return chatRooms.getOrDefault(roomName, Collections.emptyList());
    }

    // 移除用戶並清理空的聊天室
    public void removeUserFromChatRoom(String roomName, String userId, WebSocketSession session) {
        List<WebSocketSession> participants = chatRooms.get(roomName);

        if (participants != null) {
            participants.remove(session);
            System.out.println("用戶 " + userId + " 離開了聊天室 " + roomName);
            // 如果聊天室成員為空，則移除聊天室
            if (participants.isEmpty()) {
                chatRooms.remove(roomName);
            }
        }
    }

    public void saveChatMessage(String chatRoomId, int senderId, String message) {
        ChatMessage chatMessage = new ChatMessage(chatRoomId, senderId, message);
        chatMessageRepository.save(chatMessage);  // 將聊天消息保存到 MongoDB
    }

    public List<ChatMessage> getChatHistory(String roomName) {
        // 從 MongoDB 查詢該房間的歷史消息
        return chatMessageRepository.findByChatRoomId(roomName);
    }
}
