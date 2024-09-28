package com.example.chattest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatInvitationRequestDto {

    @JsonProperty("inviter_id")
    private Long inviterId;

    @JsonProperty("invitee_id")
    private Long inviteeId;

}
