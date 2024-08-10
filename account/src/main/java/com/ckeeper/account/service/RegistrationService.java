package com.ckeeper.account.service;

import com.ckeeper.account.dto.RegistrationRequest1;
import com.ckeeper.account.dto.RegistrationRequest2;
import com.ckeeper.account.dto.RegistrationRequest3;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.entity.DetailEntity;
import com.ckeeper.account.entity.KeywordEntity;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.repository.DetailRepository;
import com.ckeeper.account.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final DetailRepository detailRepository;

    @Autowired
    private final KeywordRepository keywordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(AccountRepository accountRepository,DetailRepository detailRepository,KeywordRepository keywordRepository,PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.detailRepository = detailRepository;
        this.keywordRepository = keywordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register1(RegistrationRequest1 registrationRequest1) {
        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setEmail(registrationRequest1.getEmail());
        accountEntity.setCollegeName(registrationRequest1.getCollegeName());
        accountEntity.setPassword(passwordEncoder.encode(registrationRequest1.getPassword()));

        accountRepository.save(accountEntity);
    }

    public void register2(RegistrationRequest2 registrationRequest2) {
        DetailEntity detailEntity = new DetailEntity();

        detailEntity.setEmail(registrationRequest2.getEmail());
        detailEntity.setName(registrationRequest2.getName());
        detailEntity.setNickname(registrationRequest2.getNickname());
        detailEntity.setGrade(registrationRequest2.getGrade());
        detailEntity.setDepartment1(registrationRequest2.getDepartment1());
        detailEntity.setDepartment2(registrationRequest2.getDepartment2());

        detailRepository.save(detailEntity);
    }

    public void register3(RegistrationRequest3 registrationRequest3) {
        registrationRequest3.getKeyword().forEach(item->{
            KeywordEntity keywordEntity = new KeywordEntity();

            keywordEntity.setEmail(registrationRequest3.getEmail());
            keywordEntity.setKeyword(item);

            keywordRepository.save(keywordEntity);
        });
    }
}