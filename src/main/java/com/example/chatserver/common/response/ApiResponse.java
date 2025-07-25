package com.example.chatserver.common.response;

import com.example.chatserver.common.exception.ErrorCode;

public class ApiResponse<T>{

    private String status;  // 클라이언트에게 전달되는 단순화된 결과 코드
    private String code;    // 에러 코드 (성공일 경우 null)
    private String message; // 사용자용 메시지
    private T data;


    private ApiResponse(String status, String code, String message, T data){
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 성공 응답
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>("success", null, "요청이 성공적으로 처리되었습니다.", data);
    }

    // 성공 응답 - 메시지 커스텀
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>("success", null, message, data);
    }

    // 실패 응답 - 메시지
    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>("fail", errorCode.getCode(), errorCode.getMessage(), null);
    }

    // 에러 응답 - 메시지
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>("error", errorCode.getCode(), errorCode.getMessage(), null);
    }

}
