package com.example.chatserver.member.command.controller;

import com.example.chatserver.common.response.ApiResponse;
import com.example.chatserver.member.command.dto.LoginRequest;
import com.example.chatserver.member.command.dto.SignUpRequest;
import com.example.chatserver.member.command.service.MemberService;
import com.example.chatserver.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Response Body
@RequestMapping("/member")
public class MemberController {

    private MemberService memberService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createMember(@RequestBody SignUpRequest signUpRequest){
        Long memberId = memberService.signUp(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", memberId));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> loginMember(@RequestBody LoginRequest loginRequest){
        
        // email, password 검증
        Member member = memberService.login(loginRequest);
        
        // 일치할 경우 access token 발행
        // JWT 의존성 추가 ㅣㄹ요
    }
}
