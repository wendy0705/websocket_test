package com.example.chattest.controller;

import com.example.chattest.dto.ChatInvitationRequestDto;
import com.example.chattest.entity.ChatInvitation;
import com.example.chattest.service.ChatInvitationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatInvitationController {

    private final ChatInvitationService chatInvitationService;

    @PostMapping("/invite")
    public ResponseEntity<Map<String, String>> inviteUser(@RequestBody ChatInvitationRequestDto chatInvitation) throws Exception{

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
}

