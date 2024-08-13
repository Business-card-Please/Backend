package com.ckeeper.account.service;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<AccountEntity> accountEntity = accountRepository.findById(loginRequest.getEmail());
        if(accountEntity.isPresent()){
            if(passwordEncoder.matches(loginRequest.getPassword(),accountEntity.get().getPassword())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
