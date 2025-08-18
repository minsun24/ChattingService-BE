//package com.example.chatserver.common.configs;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
///*
//    설명.
//        "/connect" 로 웹소켓 요청이 들어왔을 때 해당 요청을 처리할 클래스
//        연결 전, 연결 시, 연결 후 처리를 진행
// */
//@Slf4j
//@Component
//public class SimpleWebSocketHandler extends TextWebSocketHandler {
//
//    // TODO. 연결된 세션 관리 : 스레드 safe한 SET 사용
//    // HashSet은, thread  safe 하지 않다. => 연결이 동시에 들어왔을 때 안정적으로 모든 데이터가 저장되는 것을 보장하지 않음
//    // (로직이 꼬일 수 있음?)
//    // thread safe 한 set을 사용하기 위해서 "CorrentHashMap" 사용
//    // 동시에 여러 사용자가 서버에 커넥트를 맺는다 해도 문제 없는 자료 구조
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//
//   // TextWebSocketHandler 를 상속받아 이벤트 메서드를 오버라이딩
//
//    // TODO. 웹소켓 연결 후
//    // SET 자료구조에 사용자 정보 저장
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//        log.info("Connected to WebSocket : {}", session.getId());
//    }
//
//    // TODO. 사용자에게 메시지 전달하는 메서드
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//
//        log.info("Received text message : {}", payload);
//
//        for(WebSocketSession s : sessions){
//            if(s.isOpen()){
//                s.sendMessage(new TextMessage(payload));  // TextMessage 객체 형태로 payload 를 전송
//            }
//        }
//    }
//
//    // TODO. 웹소켓 연결 종료 후
//    // 세션을 메모리에서 삭제해 주어야 함.
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//        log.info("Disconnected from WebSocket : {}", session.getId());
//    }
//
//}
//
//
///*
//    TODO.
//    메모리에 사용자 등록
//    메시지 전달
//    연결 종료 시 메모리에서 사용자 삭제
//
// */
//
///*
//    웹 소켓 종류
//    1. 순수 웹 소켓
//    => POST맨으로 테스트 가능
//
//    2. STOMP
//    => POST맨에서는 STOMP 테스트 불가능
//    => 프론트 화면으로 테스트 해야 함.
//
//    디테일한 부분은 STOMP 로 구현.
// */