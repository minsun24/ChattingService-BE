package com.example.chatserver.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AuthErrorCode implements ErrorCode{
    UNAUTHORIZED("AUTH_401", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("AUTH_403", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_TOKEN("AUTH_401_01", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),

    EMAIL_ALREADY_EXISTS("A001", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    MEMBER_NOT_FOUND("AUTH_001", "존재하지 않는 이메일입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("AUTH_002", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED)
    ;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    AuthErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
