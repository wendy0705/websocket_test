package com.example.chattest.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "chatRooms")
public class ChatRoom {
    @Id
    private String roomName;  // 將 roomName 作為唯一的 _id
    private LocalDateTime createdAt;

    public ChatRoom(String roomName) {
        this.roomName = roomName;  // roomName 是唯一的
        this.createdAt = LocalDateTime.now();
    }

}

