package com.example.chatserver.member.command.service;

import com.example.chatserver.common.exception.AuthErrorCode;
import com.example.chatserver.common.exception.BaseException;
import com.example.chatserver.member.command.dto.LoginRequest;
import com.example.chatserver.member.command.dto.SignUpRequest;
import com.example.chatserver.member.command.repository.MemberRepository;
import com.example.chatserver.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원 가입
    public Long signUp(SignUpRequest signUpRequest) {
        
        // 이미 가입되어 있는 이메일 검증
        if(memberRepository.findByEmail((signUpRequest.getEmail())).isPresent()){
            throw new BaseException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }
        Member newMember = Member.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))      // 패스워드가 암호화되어 DB에 저장됨
                .build();

    
       memberRepository.save(newMember);
        return newMember.getId();
    }

    // 로그인
    public Member login(LoginRequest loginRequest) {
        // 멤버 조회
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BaseException(AuthErrorCode.MEMBER_NOT_FOUND));

        // DTO(비밀번호 평문) <-> DB(암호화)
        if(!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())){
            throw new BaseException(AuthErrorCode.INVALID_PASSWORD);
        }

        return member;
    }
}
