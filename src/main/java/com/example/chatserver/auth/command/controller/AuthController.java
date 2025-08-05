package com.example.chatserver.auth.command.controller;

import com.example.chatserver.auth.command.dto.EmailRequest;
import com.example.chatserver.auth.command.dto.EmailVerifyRequest;
import com.example.chatserver.auth.command.service.EmailService;
import com.example.chatserver.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailService emailService;

    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<?>> sendEmail(@RequestBody @Valid EmailRequest request) {
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("이메일 인증 코드가 전송되었습니다."));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<?>> verifyEmail(@RequestBody @Valid EmailVerifyRequest request) {

        emailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiResponse.success("이메일 인증 성공"));
    }
}
