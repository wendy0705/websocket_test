package com.example.chattest.service;

import com.example.chattest.entity.ChatInvitation;
import com.example.chattest.manager.WebSocketSessionManager;
import com.example.chattest.repository.ChatInvitationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChatInvitationService {

    private final ChatInvitationRepository chatInvitationRepository;
    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    public ChatInvitation createInvitation(Long inviterId, Long inviteeId) {

        ChatInvitation invitation = new ChatInvitation();
        invitation.setInviterId(inviterId);
        invitation.setInviteeId(inviteeId);
        invitation.setInvitationStatus("pending");
        invitation.setInviteTime(LocalDateTime.now());

        return chatInvitationRepository.save(invitation);
    }

    public boolean acceptInvitation(Long inviterId, Long inviteeId) {
        Optional<ChatInvitation> optionalInvitation = chatInvitationRepository.findByInviterIdAndInviteeId(inviterId, inviteeId);
        if (optionalInvitation.isPresent()) {
            ChatInvitation invitation = optionalInvitation.get();
            if ("pending".equals(invitation.getInvitationStatus())) {
                invitation.setInvitationStatus("accepted");
                chatInvitationRepository.save(invitation);
                return true;
            }
        }
        return false;
    }

    public boolean declineInvitation(Long inviterId, Long inviteeId) {
        Optional<ChatInvitation> optionalInvitation = chatInvitationRepository.findByInviterIdAndInviteeId(inviterId, inviteeId);
        if (optionalInvitation.isPresent()) {
            ChatInvitation invitation = optionalInvitation.get();
            if ("pending".equals(invitation.getInvitationStatus())) {
                invitation.setInvitationStatus("declined");
                chatInvitationRepository.save(invitation);
                return true;
            }
        }
        return false;
    }

    public void notifyInvitee(Long inviterId, Long inviteeId) throws Exception {
        WebSocketSession session = sessionManager.getSession(inviteeId);  // 使用 inviteeId 找到 WebSocketSession

        if (session != null && session.isOpen()) {
            // 創建邀請消息
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("type", "invitation");
            messageData.put("inviter_id", inviterId);
            messageData.put("invitee_id", inviteeId);

            String message = objectMapper.writeValueAsString(messageData); // 將消息轉換為 JSON

            // 發送 WebSocket 消息給 invitee
            session.sendMessage(new TextMessage(message));
        } else {
            System.out.println("User " + inviteeId + " is not connected to WebSocket.");
        }
    }

    public void notifyInviter(Long inviterId, Long inviteeId) throws Exception {
        WebSocketSession session = sessionManager.getSession(inviterId);  // 使用 inviterId 找到 WebSocketSession

        if (session != null && session.isOpen()) {
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("type", "accepted");
            messageData.put("invitee_id", inviteeId);

            String message = objectMapper.writeValueAsString(messageData);
            // 發送 WebSocket 消息給邀請者
            session.sendMessage(new TextMessage(message));
        } else {
            System.out.println("Inviter " + inviterId + " is not connected to WebSocket.");
        }
    }

}

