package com.ckeeper.chat.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof org.springframework.http.server.ServletServerHttpRequest){
            HttpServletRequest servletRequest = ((org.springframework.http.server.ServletServerHttpRequest) request).getServletRequest();
            URI uri = request.getURI();

            String nickname = getQueryParameter(uri, "nickname");
            if (nickname != null) {
                attributes.put("nickname", nickname);
                System.out.println("WebSocket 핸드셰이크 - nickname: " + nickname);
            }
        }
        return true;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 핸드셰이크 이후의 로직 추가해도됨ㅇㅇ.
    }

    private String getQueryParameter(URI uri, String param) {
        String query = uri.getQuery();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length > 1 && keyValue[0].equals(param)) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }
}
