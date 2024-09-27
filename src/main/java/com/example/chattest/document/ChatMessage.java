package com.example.chattest.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "chatMessages")
public class ChatMessage {
    @Id
    private String id;
    private String chatRoomId;
    private int senderId;
    private String message;
    private LocalDateTime timestamp;

    public ChatMessage(String chatRoomId, int senderId, String message) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
}

