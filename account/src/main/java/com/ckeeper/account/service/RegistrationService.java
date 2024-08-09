package com.ckeeper.account.service;

import com.ckeeper.account.dto.RegistrationRequest1;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final AccountRepository accountRepository;

    @Autowired
    public RegistrationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void register1(RegistrationRequest1 registrationRequest1) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail(registrationRequest1.getEmail());
        accountEntity.setCollegeName(registrationRequest1.getCollegeName());
        accountEntity.setPassword(registrationRequest1.getPassword());
        accountRepository.save(accountEntity);
    }
}