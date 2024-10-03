package com.ckeeper.chat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String nickname = (String) session.getAttributes().get("nickname");
        if(nickname!=null){
            sessions.put(nickname,session);
            System.out.println("WebSocket 연결 성공 - nickname: " + nickname);
        }else {
            // nickname이 없으면 WebSocket 연결을 종료
            System.out.println("nickname이 없어서 WebSocket 연결 종료");
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("nickname이 없습니다."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String nickname = (String) session.getAttributes().get("nickname");
        if (nickname != null) {
            sessions.remove(nickname);
            System.out.println("WebSocket 연결 종료 - nickname: " + nickname);
        }
    }
}
