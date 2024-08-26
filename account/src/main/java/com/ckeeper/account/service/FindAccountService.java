package com.ckeeper.account.service;

import com.ckeeper.account.dto.GenerateAuthCodeRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.repository.AccountRepository;
import com.ckeeper.account.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindAccountService {
    private final MailUtil mailUtil;
    private final AccountRepository accountRepository;

    @Autowired
    public FindAccountService(MailUtil mailUtil,AccountRepository accountRepository) {
        this.mailUtil = mailUtil;
        this.accountRepository = accountRepository;
    }

    public void findNickname(GenerateAuthCodeRequest generateAuthCodeRequest){
        AccountEntity entity = accountRepository.findNicknameByEmail(generateAuthCodeRequest.getEmail());
        mailUtil.sendMailNickname(generateAuthCodeRequest,entity.getNickname());
    }
}
