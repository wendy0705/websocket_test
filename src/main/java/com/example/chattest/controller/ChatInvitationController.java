package com.example.chattest.controller;

import com.example.chattest.dto.ChatInvitationRequestDto;
import com.example.chattest.dto.InvitationStatusRequestDto;
import com.example.chattest.dto.InvitationStatusResponseDto;
import com.example.chattest.entity.ChatInvitation;
import com.example.chattest.repository.ChatInvitationRepository;
import com.example.chattest.service.ChatInvitationService;
import com.example.chattest.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatInvitationController {

    private final ChatInvitationService chatInvitationService;
    private final ChatService chatService;
    private final ChatInvitationRepository chatInvitationRepository;

    @PostMapping("/invite")
    public ResponseEntity<Map<String, String>> inviteUser(@RequestBody ChatInvitationRequestDto chatInvitation) throws Exception{
        log.info("chatInvitation: {}", chatInvitation);

        chatInvitationService.createInvitation(chatInvitation.getInviterId(), chatInvitation.getInviteeId());
        chatInvitationService.notifyInvitee(chatInvitation.getInviterId(), chatInvitation.getInviteeId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invitation created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/accept")
    public ResponseEntity<Map<String, String>> acceptInvitation(@RequestBody ChatInvitationRequestDto chatInvitation) throws Exception {

        Long inviterId = chatInvitation.getInviterId();
        Long inviteeId = chatInvitation.getInviteeId();
        chatInvitationService.acceptInvitation(inviterId, inviteeId);
        chatInvitationService.notifyInviter(inviterId, inviteeId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invitation accepted");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/decline")
    public ResponseEntity<Map<String, String>> declineInvitation(@RequestBody ChatInvitationRequestDto chatInvitation) {
        chatInvitationService.declineInvitation(chatInvitation.getInviterId(), chatInvitation.getInviteeId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Invitation declined");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/invitation-status")
    public ResponseEntity<?> getInvitationStatuses(@RequestBody InvitationStatusRequestDto request) {
        List<InvitationStatusResponseDto> statuses = chatService.getInvitationStatuses(request.getMyId(), request.getUserIds());
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/chatrooms")
    public List<Long> getChatRoomsForCurrentUser(@RequestParam Long userId) {
        List<Long> otherUserIds = chatInvitationRepository.findOtherUserIds(userId);
        log.info(otherUserIds.toString());
        return otherUserIds;
    }
}

