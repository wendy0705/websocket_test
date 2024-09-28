package com.example.chattest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chat_invitation")
public class ChatInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "inviter_id")
    private Long inviterId;

    @Column(name = "invitee_id")
    private Long inviteeId;

    @Column(name = "invitation_status")
    private String invitationStatus;  // 'pending', 'accepted', 'declined'

    @Column(name = "invite_time")
    private LocalDateTime inviteTime;
}
