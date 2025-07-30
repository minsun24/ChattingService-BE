package com.example.chatserver.member.command.controller;

import com.example.chatserver.common.auth.JwtTokenProvider;
import com.example.chatserver.common.response.ApiResponse;
import com.example.chatserver.member.command.dto.LoginRequest;
import com.example.chatserver.member.command.dto.LoginResponse;
import com.example.chatserver.member.command.dto.MemberListResponse;
import com.example.chatserver.member.command.dto.SignUpRequest;
import com.example.chatserver.member.command.service.MemberService;
import com.example.chatserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController // Response Body
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("🔥 [Controller] /health 호출됨");
        log.info("✅ [Controller] /health 호출됨");
        return ResponseEntity.ok("health");
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> createMember(@RequestBody SignUpRequest signUpRequest){
        log.info("회원가입 요청: {}", signUpRequest.getEmail());
        Long memberId = memberService.signUp(signUpRequest);
        log.info("회원가입 완료: memberId={}", memberId);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", memberId));
    }

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

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MemberListResponse>>> getMemberList(){

        List<MemberListResponse> memberList = memberService.findAllMembers();
        return ResponseEntity.ok(ApiResponse.success("멤버 목록 조회 성공", memberList));
    }




}
