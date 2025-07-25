package com.example.chatserver.member.command.controller;

import com.example.chatserver.common.response.ApiResponse;
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
        Member member = memberService.signUp(signUpRequest);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", member.getId()));
    }
}
