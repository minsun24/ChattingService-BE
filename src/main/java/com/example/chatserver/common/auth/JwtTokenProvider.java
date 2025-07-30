package com.example.chatserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey;

    private final int expiration;

    private final Key SECRET_KEY;

    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expiration}")int expiration) {
        this.secretKey = secretKey;
        this.expiration = expiration;
        SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS512.getJcaName());
        // 1. secretKey 를 디코딩 -> (chatserver ... ) 평문이 나오게 됨
        // 2. 평문을 HS512 알고리즘으로 암호화
    }

    // 토큰 생성
    // 토큰 생성 시 매개변수로 email, role 을 전달해야 함.
    public String createToken(String email, String role){
        // Claims (== PAYLOAD)
        // subject 가 필수, 키 값

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)   // 발행 시간
                .setExpiration(new Date(now.getTime()+expiration*60*100L))      // 만료일자
                .signWith(SECRET_KEY)   // 암호화된 시크릿 키로 서명
                .compact();

        return token;
    }



}

/*
    ## JWT Token
    HEADER
    > 토큰 메타 정보
    - 토큰이 어던 알고리즘이 사용 됏는지
    - JWT 토큰인지 아닌지 (타입)

    PAYLOAD
    > 실질적 데이터
    - 이름, 이메일, 역할 ...
    - 사용자 정의
    - TOKEN 속 페이로드로 정보를 알 수 있다.
    - 노출 불가한 보안 정보는 포함되면 안됨

    SIGNATURE
    > 토큰 검증
    - 헤더, 페이로드 + 서버 비밀키(SECRET KEY) 를 암호화 처리
    - 지정한 알고리즘을 가지고 암호화된 값
    - 복호화시킬 수 없는 값.

    => HEADER, PAYLOAD, SIGNATURE 를 한꺼번에 인코딩(복호화 가능) => JWT Token

    디코딩하여 HEADER, PAYLOAD 는 확인 가능
 */