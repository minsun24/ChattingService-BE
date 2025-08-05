package com.example.chatserver.auth.command.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class EmailRequest {
    @Email
    @NotBlank
    private String email;
}