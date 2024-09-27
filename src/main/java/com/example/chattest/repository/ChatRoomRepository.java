package com.example.chattest.repository;

import com.example.chattest.document.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    // 你可以定義一些查詢方法，如通過參與者ID查找聊天室
    Optional<ChatRoom> findByRoomName(String roomName);
}
