package com.ckeeper.account.service;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.entity.DetailEntity;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.repository.DetailRepository;
import com.ckeeper.account.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EtcService {
    private final DetailRepository detailRepository;
    private final JwtUtil jwtUtil;
    private LoginService loginService;

    public EtcService(DetailRepository detailRepository,JwtUtil jwtUtil, LoginService loginService) {
        this.detailRepository = detailRepository;
        this.jwtUtil = jwtUtil;
        this.loginService = loginService;
    }

    public Optional<DetailEntity> getAccountInfo(HttpServletRequest request){
        String accessToken = loginService.getTokenValue(request,"access_token");
        Claims claims = this.jwtUtil.validateToken(accessToken);
        return detailRepository.findByNickname(claims.getSubject());
    }

    public Optional<DetailEntity> getInfo(LoginRequest loginRequest){
        return detailRepository.findByNickname(loginRequest.getNickname());
    }
}
