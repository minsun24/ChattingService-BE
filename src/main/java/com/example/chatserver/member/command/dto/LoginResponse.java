package com.example.chatserver.member.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long memberId;
    private String email;
    private String accessToken;
//    private String refreshToken;
    private String role;
}
