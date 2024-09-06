package com.ckeeper.rentalboard.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtUtil {
    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims validateToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getTokenValue(HttpServletRequest request, String tokenName){
        Cookie[] cookie = request.getCookies();
        if(cookie != null){
            for(Cookie cookieEntity : cookie){
                if(cookieEntity.getName().equals(tokenName)){
                    return cookieEntity.getValue();
                }
            }
        }
        return null;
    }
}
