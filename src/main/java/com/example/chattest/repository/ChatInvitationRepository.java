package com.example.chattest.repository;

import com.example.chattest.entity.ChatInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatInvitationRepository extends JpaRepository<ChatInvitation, Long> {
    // 基於 inviterId 和 inviteeId 查找邀請
    Optional<ChatInvitation> findByInviterIdAndInviteeId(Long inviterId, Long inviteeId);
    List<ChatInvitation> findByInviterIdAndInviteeIdIn(Long inviterId, List<Long> inviteeIds);
    @Query("SELECT CASE WHEN c.inviterId = :userId THEN c.inviteeId ELSE c.inviterId END " +
            "FROM ChatInvitation c " +
            "WHERE (c.inviterId = :userId OR c.inviteeId = :userId) " +
            "AND c.invitationStatus = 'accepted'")
    List<Long> findOtherUserIds(@Param("userId") Long userId);


}

