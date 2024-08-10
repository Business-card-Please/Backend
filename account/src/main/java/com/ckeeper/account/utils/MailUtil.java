package com.ckeeper.account.utils;

import com.ckeeper.account.dto.AuthCodeRequest;
import com.ckeeper.account.dto.GenerateAuthCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class MailUtil {
    private final JavaMailSender javaMailSender;
    private final CacheService cacheService;
    private static final String CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    public class MailConstants{
        private static final String EMAIL_FROM = "seop0937@gmail.com";
        private static final String EMAIL_SUBJECT = "인증 코드";
        private static final String EMAIL_TEXT = "인증 코드는 ";
    }

    @Autowired
    public MailUtil(JavaMailSender javaMailSender,CacheService cacheService) {
        this.javaMailSender = javaMailSender;
        this.cacheService = cacheService;
    }

    public void sendMail(GenerateAuthCodeRequest generateAuthCodeRequest){
        SimpleMailMessage msg = new SimpleMailMessage();
        String authCode = generateAuthCode(6);

        msg.setFrom(MailConstants.EMAIL_FROM);
        msg.setTo(generateAuthCodeRequest.getEmail());
        msg.setSubject(MailConstants.EMAIL_SUBJECT);
        msg.setText(MailConstants.EMAIL_TEXT+authCode);
        javaMailSender.send(msg);

        cacheService.saveAuthCode(generateAuthCodeRequest.getEmail(),authCode);
    }

    private String generateAuthCode(Integer digit) {
        StringBuilder code = new StringBuilder(digit);
        for (int i = 0; i < digit; i++) {
            code.append(CHAR.charAt(secureRandom.nextInt(CHAR.length())));
        }
        return code.toString();
    }

    public Boolean matchAuthCode(AuthCodeRequest authCodeRequest){
        String targetAuthCode = cacheService.getAuthCode(authCodeRequest.getEmail());
        return targetAuthCode.equals(authCodeRequest.getAuthCode());
    }
}
