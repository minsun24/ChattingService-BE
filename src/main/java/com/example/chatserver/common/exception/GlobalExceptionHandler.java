package com.example.chatserver.common.exception;

import com.example.chatserver.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
    @ControllerAdvice 을 활용한 Global Exception Handler
    요청~응답 흐름 도중에 예외가 발생하면 여기로 점프
    응답 형태, 상태 코드 커스텀 가능

 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 예외 처리 (ErrorCode 기반)
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode));
    }

    // 유효성 검사 실패 (@Valid) -> @Valid 실패 시, 첫 번째 오류 메시지를 응답
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        // 메시지는 바디에서 추출하지만, 코드는 공통 VALIDATION_ERROR로 고정
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(CommonErrorCode.VALIDATION_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, Exception e) {
        e.printStackTrace(); // or log.error

        if ("text/event-stream".equals(request.getHeader("Accept"))) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // SSE용 처리
        }

        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ApiResponse.error(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }

}
