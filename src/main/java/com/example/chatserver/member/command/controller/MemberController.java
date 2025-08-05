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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> createMember(@RequestBody SignUpRequest signUpRequest){
        log.info("회원가입 요청: {}", signUpRequest.getEmail());
        Long memberId = memberService.signUp(signUpRequest);
        log.info("회원가입 완료: memberId={}", memberId);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", memberId));
    }

//    회원 목록 전체 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MemberListResponse>>> getMemberList(){

        List<MemberListResponse> memberList = memberService.findAllMembers();
        return ResponseEntity.ok(ApiResponse.success("멤버 목록 조회 성공", memberList));
    }




}
