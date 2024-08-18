package com.ckeeper.account.service;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.repository.AccountRepository;
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
    public LoginService(AccountRepository accountRepository,PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean checkLogin(LoginRequest loginRequest){
        return accountRepository.findById(loginRequest.getEmail())
                .map(accountEntity -> passwordEncoder.matches(loginRequest.getPassword(),accountEntity.getPassword()))
                .orElse(false);
    }
}
