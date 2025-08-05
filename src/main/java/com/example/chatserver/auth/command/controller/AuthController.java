package com.example.chatserver.auth.command.controller;

import com.example.chatserver.auth.command.dto.EmailRequest;
import com.example.chatserver.auth.command.dto.EmailVerifyRequest;
import com.example.chatserver.auth.command.service.EmailService;
import com.example.chatserver.common.auth.JwtTokenProvider;
import com.example.chatserver.common.response.ApiResponse;
import com.example.chatserver.member.command.dto.LoginRequest;
import com.example.chatserver.member.command.dto.LoginResponse;
import com.example.chatserver.member.command.service.MemberService;
import com.example.chatserver.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;


    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginMember(@RequestBody LoginRequest loginRequest){

        log.info("로그인 요청: {}", loginRequest.getEmail());

        // email, password 검증
        Member member = memberService.login(loginRequest);

        // 일치할 경우 access token 발행
        // JWT 의존성 추가 필요
        String accessToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        // Refresh token

        LoginResponse response = new LoginResponse(
                member.getId(),
                member.getEmail(),
                accessToken,
//                refreshToken,
                member.getRole().toString()
        );
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
    }


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
