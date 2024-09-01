package com.ckeeper.account.utils;

import com.ckeeper.account.dto.AuthCodeRequest;
import com.ckeeper.account.dto.GenerateAuthCodeRequest;
import com.ckeeper.account.exception.InternalServerException;
import com.ckeeper.account.exception.InvalidAuthCodeException;
import com.ckeeper.account.exception.MailSendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
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
        private static final String EMAIL_FROM = "TEAM.c-Keeper";
        private static final String EMAIL_SUBJECT_0 = "인증 코드";
        private static final String EMAIL_SUBJECT_1 = "아이디 찾기";
        private static final String EMAIL_TEXT_0 = "인증 코드는 ";
        private static final String EMAIL_TEXT_1 = "당신의 닉네임은 ";
    }

    @Autowired
    public MailUtil(JavaMailSender javaMailSender,CacheService cacheService) {
        this.javaMailSender = javaMailSender;
        this.cacheService = cacheService;
    }

    public void sendMailAuth(GenerateAuthCodeRequest generateAuthCodeRequest){
        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            String authCode = generateAuthCode(6);

            msg.setFrom(MailConstants.EMAIL_FROM);
            msg.setTo(generateAuthCodeRequest.getEmail());
            msg.setSubject(MailConstants.EMAIL_SUBJECT_0);
            msg.setText(MailConstants.EMAIL_TEXT_0+authCode);

            javaMailSender.send(msg);
            cacheService.saveAuthCode(generateAuthCodeRequest.getEmail(),authCode);
        }catch(MailException e){
            throw new MailSendException("Failed to send email: " + e.getMessage());
        }catch(Exception e){
            throw new InternalServerException("An unexpected error occurred",e);
        }
    }

    public void sendMailNickname(GenerateAuthCodeRequest generateAuthCodeRequest, String nickname){
        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(MailConstants.EMAIL_FROM);
            msg.setTo(generateAuthCodeRequest.getEmail());
            msg.setSubject(MailConstants.EMAIL_SUBJECT_1);
            msg.setText(MailConstants.EMAIL_TEXT_1+nickname);

            javaMailSender.send(msg);
        }catch(MailException e){
            throw new MailSendException("Failed to send email: " + e.getMessage());
        }catch(Exception e){
            throw new InternalServerException("An unexpected error occurred",e);
        }
    }

    private String generateAuthCode(Integer digit) {
        StringBuilder code = new StringBuilder(digit);
        for (int i = 0; i < digit; i++) {
            code.append(CHAR.charAt(secureRandom.nextInt(CHAR.length())));
        }
        return code.toString();
    }

    public Boolean matchAuthCode(AuthCodeRequest authCodeRequest){
        try{
            String targetAuthCode = cacheService.getAuthCode(authCodeRequest.getEmail());
            if(targetAuthCode == null){
                throw new InvalidAuthCodeException();
            }
            return targetAuthCode.equals(authCodeRequest.getAuthCode());

        }catch(Exception e){
            throw new InternalServerException("An unexpected error occurred",e);
        }

    }
}
