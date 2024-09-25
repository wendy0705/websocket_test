package com.example.chattest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Service
public class ChatService {

    // 存放聊天室中的用戶，這裡我們以 Map<String, List<WebSocketSession>> 模擬多個聊天室
    private Map<String, List<WebSocketSession>> chatRooms = new HashMap<>();

    // 將用戶加入聊天室
    public void addUserToChatRoom(String username, WebSocketSession session) {
        // 我們假設所有用戶都在一個公共聊天室中，聊天室名稱為 "defaultRoom"
        String roomName = "defaultRoom";
        chatRooms.putIfAbsent(roomName, new ArrayList<>()); // 如果聊天室不存在則創建
        chatRooms.get(roomName).add(session);
        System.out.println("用戶 " + username + " 加入了聊天室 " + roomName);
    }

    // 取得聊天室內的成員（除了自己以外的成員）
    public List<WebSocketSession> getChatRoomParticipants(String username) {
        String roomName = "defaultRoom"; // 假設所有用戶都在同一個聊天室
        return chatRooms.getOrDefault(roomName, Collections.emptyList());
    }

    // 移除用戶並清理空的聊天室
    public void removeUserFromChatRoom(String username, WebSocketSession session) {
        String roomName = "defaultRoom"; // 假設所有用戶都在同一個聊天室
        List<WebSocketSession> participants = chatRooms.get(roomName);
        if (participants != null) {
            participants.remove(session);
            System.out.println("用戶 " + username + " 離開了聊天室 " + roomName);
            // 如果聊天室成員為空，則移除聊天室
            if (participants.isEmpty()) {
                chatRooms.remove(roomName);
            }
        }
    }
}
