package com.example.chatserver.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements ErrorCode{
    MEMBER_LIST_EMPTY("MEMBER_001", "조회된 멤버가 없습니다.", HttpStatus.NOT_FOUND)
    ;



    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    MemberErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
