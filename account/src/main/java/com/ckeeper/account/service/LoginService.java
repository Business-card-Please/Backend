package com.ckeeper.account.service;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.utils.JwtUtil;
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

    public Boolean checkLogin(LoginRequest loginRequest, HttpServletResponse response){
        return accountRepository.findById(loginRequest.getEmail())
                .map(accountEntity -> {
                    boolean check = passwordEncoder.matches(loginRequest.getPassword(),accountEntity.getPassword());
                    if(check){jwtUtil.addJwtTokens(response,loginRequest.getEmail());}
                    return check;
                })
                .orElse(false);
    }


}
