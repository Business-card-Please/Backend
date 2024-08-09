package com.ckeeper.account.service;

import com.ckeeper.account.dto.RegistrationRequest1;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(AccountRepository accountRepository,PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register1(RegistrationRequest1 registrationRequest1) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail(registrationRequest1.getEmail());
        accountEntity.setCollegeName(registrationRequest1.getCollegeName());
        accountEntity.setPassword(passwordEncoder.encode(registrationRequest1.getPassword()));
        accountRepository.save(accountEntity);
    }
}