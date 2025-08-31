package com.example.chatserver.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class StompController {

//    메시지 브로커 역할
    @MessageMapping("/{roomId}")    // 클라이언트에서 특정 publish/roomId로 메시지 발행 시, MessageMapping이 수신
    @SendTo("/topic/{roomId}")      // 해당 roomId에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
    public String sendMessage(@DestinationVariable Long roomId, String message) {
        // DestinationVariable : @MessageMapping 어노테이션으로 정의된 WebSocket Controller 내에서만 사용
        log.info("📩 Received message in room {}: {}", roomId, message);

        if (message == null || message.isBlank()) {
            log.warn("⚠️ Empty message received in room {}", roomId);
        }
        log.info("📤 Broadcasting to /topic/{}", roomId);
        return message;
    }

}

/*
    흐름
    WebSocket Config
    /publish/번호 -> message mapping 이 있는 곳으로 와서, room id가 값이 정해짐. 
    
 */