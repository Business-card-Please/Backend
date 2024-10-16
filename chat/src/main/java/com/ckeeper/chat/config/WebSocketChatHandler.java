package com.ckeeper.chat.config;

import com.ckeeper.chat.dto.MessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {
    private Map<String, WebSocketSession> sessions = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String nickname = (String) session.getAttributes().get("nickname");
        if(nickname!=null){
            sessions.put(nickname,session);
        }else {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Not found nickname."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String nickname = (String) session.getAttributes().get("nickname");
        if (nickname != null) {
            sessions.remove(nickname);
            System.out.println("WebSocket disconnected - nickname: " + nickname);
        }
    }

    public void handleSendMessage(WebSocketSession session, MessageRequest msg) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(msg);
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WebSocketSession getSession(String nickname){
        return sessions.get(nickname);
    }
}
