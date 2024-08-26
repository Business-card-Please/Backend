package com.ckeeper.account.service;

import com.ckeeper.account.dto.RegistrationRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.entity.DetailEntity;
import com.ckeeper.account.entity.KeywordEntity;
import com.ckeeper.account.exception.InternalServerException;
import com.ckeeper.account.exception.InvalidRequestException;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.repository.DetailRepository;
import com.ckeeper.account.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void register(RegistrationRequest dto) {
        try{
            setAccount(dto.getNickname(),dto.getEmail(), dto.getCollegeName(), dto.getPassword());
            setDetail(dto.getNickname(),dto.getName(),dto.getGrade(),dto.getDepartment1(),dto.getDepartment2());
            setKeyword(dto.getNickname(),dto.getKeywords());
        }catch(DataIntegrityViolationException e){
            throw new InvalidRequestException("Data Integrity Violation: "+e.getMessage());
        }catch(IllegalArgumentException e){
            throw new InvalidRequestException("Illegal Argument: "+e.getMessage());
        }catch(Exception e){
            throw new InternalServerException("An unexpected error occurred",e);
        }
    }

    public void setAccount(String nickname,String email,String collegeName,String password) {
        if(accountRepository.existsByNickname(nickname)) {
            throw new DataIntegrityViolationException("Nickname already exists");
        }
        if(accountRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setNickname(nickname);
        accountEntity.setEmail(email);
        accountEntity.setCollegeName(collegeName);
        accountEntity.setPassword(passwordEncoder.encode(password));

        accountRepository.save(accountEntity);
    }

    public void setDetail(String nickname, String name,Short grade, String department1, String department2) {
        if(detailRepository.existsByNickname(nickname)) {
            throw new DataIntegrityViolationException("Nickname already exists");
        }

        DetailEntity detailEntity = new DetailEntity();

        detailEntity.setNickname(nickname);
        detailEntity.setName(name);
        detailEntity.setGrade(grade);
        detailEntity.setDepartment1(department1);
        detailEntity.setDepartment2(department2);

        detailRepository.save(detailEntity);
    }

    public void setKeyword(String nickname, List<String> keywords) {
        keywords.forEach(keyword->{
            KeywordEntity keywordEntity = new KeywordEntity();

            keywordEntity.setNickname(nickname);
            keywordEntity.setKeyword(keyword);

            keywordRepository.save(keywordEntity);
        });
    }
}