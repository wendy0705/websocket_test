package com.example.chattest.repository;

import com.example.chattest.entity.ChatInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatInvitationRepository extends JpaRepository<ChatInvitation, Long> {
    // 基於 inviterId 和 inviteeId 查找邀請
    Optional<ChatInvitation> findByInviterIdAndInviteeId(Long inviterId, Long inviteeId);
}

