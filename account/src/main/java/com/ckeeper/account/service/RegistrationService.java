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
            setAccount(dto.getEmail(), dto.getCollegeName(), dto.getPassword());
            setDetail(dto.getEmail(),dto.getName(),dto.getNickname(),dto.getGrade(),dto.getDepartment1(),dto.getDepartment2());
            setKeyword(dto.getEmail(),dto.getKeywords());
        }catch(DataIntegrityViolationException e){
            throw new InvalidRequestException("Data Integrity Violation: "+e.getMessage());
        }catch(IllegalArgumentException e){
            throw new InvalidRequestException("Illegal Argument: "+e.getMessage());
        }catch(Exception e){
            throw new InternalServerException("An unexpected error occurred",e);
        }
    }

    public void setAccount(String email,String collegeName,String password) {
        if(accountRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        if(collegeName == null || collegeName.isEmpty() || collegeName.contains(" ")){
            throw new IllegalArgumentException("College name contains illegal characters");
        }
        if(password == null || password.isEmpty() || password.contains(" ")){
            throw new IllegalArgumentException("Password contains illegal characters");
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setEmail(email);
        accountEntity.setCollegeName(collegeName);
        accountEntity.setPassword(passwordEncoder.encode(password));

        accountRepository.save(accountEntity);
    }

    public void setDetail(String email, String name, String nickname, Short grade, String department1, String department2) {
        if(detailRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        if (!email.contains("@") || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (name == null || name.isEmpty() || name.contains(" ")) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        if(nickname == null || nickname.isEmpty()){
            throw new IllegalArgumentException("Nickname contains illegal characters");
        }
        if (grade != null && (grade < 1 || grade > 5)) {
            throw new IllegalArgumentException("Grade must be between 1 and 5");
        }
        if (department1 == null || department1.isEmpty() || department1.contains(" ")) {
            throw new IllegalArgumentException("Department must not be null or empty");
        }

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
        if (!email.contains("@") || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if(keywords != null && !keywords.isEmpty()) {return;}

        keywords.forEach(keyword->{
            KeywordEntity keywordEntity = new KeywordEntity();

            keywordEntity.setEmail(email);
            keywordEntity.setKeyword(keyword);

            keywordRepository.save(keywordEntity);
        });
    }
}