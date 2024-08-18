package com.ckeeper.account.service;

import com.ckeeper.account.dto.GenerateAuthCodeRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.repository.DetailRepository;
import com.ckeeper.account.repository.KeywordRepository;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final DetailRepository detailRepository;

    @Autowired
    private final KeywordRepository keywordRepository;

    @Autowired
    public TestService(AccountRepository accountRepository,DetailRepository detailRepository, KeywordRepository keywordRepository) {
        this.accountRepository = accountRepository;
        this.detailRepository = detailRepository;
        this.keywordRepository = keywordRepository;
    }

    public List<AccountEntity> selectCurrentAccountInfo(){
        List<AccountEntity> accounts = accountRepository.findAll();
        return accounts;
    }

    @Transactional
    public void deleteAccountInfo(GenerateAuthCodeRequest generateAuthCodeRequest){
        String email = generateAuthCodeRequest.getEmail();

        keywordRepository.deleteAllByEmail(email);
        detailRepository.deleteAllByEmail(email);
        accountRepository.deleteById(email);
    }
}
