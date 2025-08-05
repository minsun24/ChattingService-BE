package com.example.chatserver.auth.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailVerifyRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String code;
}
