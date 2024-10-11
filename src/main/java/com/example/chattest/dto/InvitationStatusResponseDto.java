package com.example.chattest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationStatusResponseDto {
    private Long userId;
    private String status;
}
