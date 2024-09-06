package com.ckeeper.account.service;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginService(AccountRepository accountRepository,PasswordEncoder passwordEncoder,JwtUtil jwtUtil){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Boolean checkAccount(LoginRequest loginRequest, HttpServletResponse response){
        return accountRepository.findById(loginRequest.getNickname())
                .map(accountEntity -> {
                    boolean check = passwordEncoder.matches(loginRequest.getPassword(),accountEntity.getPassword());
                    if(check){jwtUtil.addJwtTokens(response,loginRequest.getNickname());}
                    return check;
                })
                .orElse(false);
    }

    public Boolean checkLogin(HttpServletRequest request,HttpServletResponse response){
        try{
            String accessToken = getTokenValue(request,"access_token");
            String refreshToken = getTokenValue(request,"refresh_token");

            if(accessToken != null){
                try{
                    Claims claims = jwtUtil.validateToken(accessToken);
                    return true;
                }catch(ExpiredJwtException e){
                    if(refreshToken != null){
                        Claims refreshClaims = jwtUtil.validateToken(refreshToken);
                        jwtUtil.addJwtAccessToken(response,refreshClaims.getSubject());
                        return true;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
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
