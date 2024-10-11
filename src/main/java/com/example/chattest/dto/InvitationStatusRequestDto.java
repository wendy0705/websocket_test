package com.example.chattest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationStatusRequestDto {
    private Long myId;
    private List<Long> userIds;
}
