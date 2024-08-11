package com.ckeeper.account.service;

import com.ckeeper.account.dto.RegistrationRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.entity.DetailEntity;
import com.ckeeper.account.entity.KeywordEntity;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.repository.DetailRepository;
import com.ckeeper.account.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void register(RegistrationRequest dto) {
        setAccount(dto.getEmail(), dto.getCollegeName(), dto.getPassword());
        setDetail(dto.getEmail(),dto.getName(),dto.getNickname(),dto.getGrade(),dto.getDepartment1(),dto.getDepartment2());
        setKeyword(dto.getEmail(),dto.getKeywords());
    }

    public void setAccount(String email,String collegeName,String password) {
        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setEmail(email);
        accountEntity.setCollegeName(collegeName);
        accountEntity.setPassword(passwordEncoder.encode(password));

        accountRepository.save(accountEntity);
    }

    public void setDetail(String email, String name, String nickname, Short grade, String department1, String department2) {
        DetailEntity detailEntity = new DetailEntity();

        detailEntity.setEmail(email);
        detailEntity.setName(name);
        detailEntity.setNickname(nickname);
        detailEntity.setGrade(grade);
        detailEntity.setDepartment1(department1);
        detailEntity.setDepartment2(department2);

        detailRepository.save(detailEntity);
    }

    public void setKeyword(String email, List<String> keywords) {
        keywords.forEach(keyword->{
            KeywordEntity keywordEntity = new KeywordEntity();

            keywordEntity.setEmail(email);
            keywordEntity.setKeyword(keyword);

            keywordRepository.save(keywordEntity);
        });
    }
}