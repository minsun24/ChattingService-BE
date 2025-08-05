package com.example.chatserver.auth.command.service;

import com.example.chatserver.common.exception.AuthErrorCode;
import com.example.chatserver.common.exception.BaseException;
import com.example.chatserver.common.exception.CommonErrorCode;
import com.example.chatserver.member.command.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;
    private final MemberService memberService;
    
    private static final long CODE_TTL_SECONDS = 180L;  // 이메일 인증 유효 시간 (3분)

    // 인증 코드 발송
    public void sendVerificationCode(String email) {
        /*
            1. 인증 코드 생성
            2. Redis에 email:code 로 저장
            3. 이메일 내용 구성 및 발송
         */

        // 이미 가입되어 있는 회원인지 확인
        if(memberService.isExistMember(email)){
            throw new BaseException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 인증 코드 이미 존재할 경우, 재요청 차단
        if (redisTemplate.hasKey(email)) {
            throw new BaseException(AuthErrorCode.TOO_MANY_REQUESTS);
        }

        // 인증 코드 생성 (6자리 난수)
        String code = String.valueOf((int) (Math.random() * 900000) + 100000);

        // (key: email, value: code)
        try{
            redisTemplate.opsForValue().set(email, code, CODE_TTL_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e){
            log.error("Redis 저장 실패", e);
            throw new BaseException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 이메일 발송 내용 구성
        String subject = "[\uD83C\uDF4ATokTang] 이메일 인증 코드입니다.";
        String text = String.format("인증 코드: %s\n 해당 코드는 3분 동안 유효합니다.", code);

        // 이메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);

        } catch (Exception e) {
            log.error("메일 전송 실패 : {} ", e);
            throw new BaseException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        log.info("✅ 인증 코드 전송 완료: {} → {}", email, code);
    }

    
    // 이메일 인증번호 확인
    public void verifyCode(String email, String code) {
        /*
            1. Redis에서 코드 조회
            2. 일치 여부 확인
            3. Redis에서 이메일 키 삭제
         */

        // Redis에서 코드 조회
        String savedCode = redisTemplate.opsForValue().get(email);

        // 일치 여부 확인
        if (savedCode == null || !savedCode.equals(code)) {
            throw new BaseException(AuthErrorCode.INVALID_VERIFICATION_CODE);
        }

        // Redis에서 키(이메일) 삭제
        redisTemplate.delete(email);
        log.info("✅ 인증 코드 확인 완료: {} → {}", email, code);
    }
}
