package com.example.chatserver.common.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/*
    "/connect" 로 웹소켓 요청이 들어왔을 때 이를 처리할 클래스
 */
@Component
public class SimpleWebSocketHandler extends TextWebSocketHandler {
}
