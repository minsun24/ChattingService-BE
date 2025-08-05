package com.example.chatserver.common.configs;

import com.example.chatserver.common.exception.CommonErrorCode;
import com.example.chatserver.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiResponse<Object> errorResponse = ApiResponse.error(CommonErrorCode.FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(CommonErrorCode.FORBIDDEN.getHttpStatus().value());

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}

/*
    AccessDeniedHandler
    > 인가(Authorization) 처리가 실패했을 때 응답을 정의하는 역할

    ex)
    - 인증은 되었지만, 요청한 리소스에 접근할 권한이 없을 때
    - ROLE_USER 계정이 ROLE_ADMIN 만 접근 가능한 API를 호출했을 때 등

    → 이 경우 Spring Security는 기본적으로 403 Forbidden 을 반환하며,
      AccessDeniedHandler 를 통해 응답 본문(JSON)도 커스터마이징 가능
 */