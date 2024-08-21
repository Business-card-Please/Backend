package com.ckeeper.account.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtil {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final SecretKey secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private String createAccessToken(String email){
        return createToken(email,accessTokenExpiration);
    }

    private String createRefreshToken(String email){
        return createToken(email,refreshTokenExpiration);
    }

    private String createToken(String email, Long expiration){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public void addJwtTokens(HttpServletResponse response,String email){
        Cookie accessTokenCookie = createCookie(ACCESS_TOKEN, createAccessToken(email), 5 * 60); // 5 minutes (300 seconds)
        Cookie refreshTokenCookie = createCookie(REFRESH_TOKEN, createRefreshToken(email),7 * 24 * 60 * 60); // 7 days (604800 seconds)

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    private Cookie createCookie(String name,String value,Integer maxAge){
        Cookie cookie = new Cookie(name,value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }
}