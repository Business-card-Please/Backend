package com.ckeeper.account.service;

import com.ckeeper.account.dto.GenerateAuthCodeRequest;
import com.ckeeper.account.dto.ResetPasswordRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindAccountService {
    private final MailUtil mailUtil;
    private final AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public FindAccountService(MailUtil mailUtil,AccountRepository accountRepository,PasswordEncoder passwordEncoder) {
        this.mailUtil = mailUtil;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void findNickname(GenerateAuthCodeRequest generateAuthCodeRequest){
        AccountEntity entity = accountRepository.findNicknameByEmail(generateAuthCodeRequest.getEmail());
        mailUtil.sendMailNickname(generateAuthCodeRequest,entity.getNickname());
    }

    public Boolean resetPassword(ResetPasswordRequest resetPasswordRequest){
        return accountRepository.findById(resetPasswordRequest.getNickname())
                .map(accountEntity -> {
                    boolean check = passwordEncoder.matches(resetPasswordRequest.getNewPassword(), accountEntity.getPassword());
                    System.out.println(check);
                    if(!check){
                        accountEntity.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
                        accountRepository.save(accountEntity);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
}
