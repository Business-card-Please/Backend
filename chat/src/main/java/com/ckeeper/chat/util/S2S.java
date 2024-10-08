package com.ckeeper.chat.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.*;

@Service
public class S2S {
    private final WebClient webClient;

    @Value("${server.checklogin}")
    private String targetUrl;

    public S2S(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl(targetUrl).build();
    }

    public String sendToAuthServer(HttpServletRequest req) {
        Optional<Cookie> accessTokenCookie = Arrays.stream(req.getCookies())
                .filter(cookie -> "access_token".equals(cookie.getName()))
                .findFirst();

        Optional<Cookie> refreshTokenCookie = Arrays.stream(req.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst();

        WebClient.RequestBodySpec requestBodySpec = webClient.post()
                .uri(targetUrl+"/account/check-login");

        if (accessTokenCookie.isPresent()) {
            requestBodySpec = requestBodySpec.cookie("access_token", accessTokenCookie.get().getValue());
        }

        if (refreshTokenCookie.isPresent()) {
            requestBodySpec = requestBodySpec.cookie("refresh_token", refreshTokenCookie.get().getValue());
        }

        Mono<String> res = requestBodySpec
                .retrieve()
                .bodyToMono(String.class);

        return res.block();
    }
}
