package com.example.chatserver.common.configs;

import com.example.chatserver.common.exception.CommonErrorCode;
import com.example.chatserver.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.Map;

@Component
public class CustomAuthentication implements AuthenticationEntryPoint {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(CommonErrorCode.UNAUTHORIZED.getHttpStatus().value()); // 401

        objectMapper.writeValue(response.getWriter(), ApiResponse.error(CommonErrorCode.UNAUTHORIZED));

    }
}

/*
    AuthenticationEntryPoint
    인증 처리가 실패했을 때 응답을 정의하는 역할
    ex)
    - 아예 JWT 토큰이 없을 때
    - 토큰이 유효하지 않을 때 (만료, 변조 ...)
    - 인증 정보가 없는 사용자가 보호된 리소스에 접근
 */