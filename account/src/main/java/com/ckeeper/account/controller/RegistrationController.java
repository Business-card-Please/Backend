package com.ckeeper.account.controller;

import com.ckeeper.account.dto.*;
import com.ckeeper.account.service.RegistrationService;
import com.ckeeper.account.utils.CacheService;
import com.ckeeper.account.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/account")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final CacheService cacheService;
    private final MailUtil mailUtil;

    @Autowired
    public RegistrationController(RegistrationService registrationService,MailUtil mailUtil,CacheService cacheService) {
        this.registrationService = registrationService;
        this.cacheService = cacheService;
        this.mailUtil = mailUtil;
    }

    @PostMapping("/registration1")
    public ResponseEntity<String> orderRegistration1(@RequestBody RegistrationRequest1 registrationRequest1) {
        try{
            registrationService.register1(registrationRequest1);
            return ResponseEntity.ok("success");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/registration2")
    public ResponseEntity<String> orderRegistration2(@RequestBody RegistrationRequest2 registrationRequest2) {
        try{
            registrationService.register2(registrationRequest2);
            return ResponseEntity.ok("success");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/registration3")
    public ResponseEntity<String> orderRegistration3(@RequestBody RegistrationRequest3 registrationRequest3) {
        try{
            registrationService.register3(registrationRequest3);
            return ResponseEntity.ok("success");
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/generate-authcode")
    public ResponseEntity<String> orderGenerateAuthCode(@RequestBody GenerateAuthCodeRequest generateAuthCodeRequest) {
        try{
            mailUtil.sendMail(generateAuthCodeRequest);
            return ResponseEntity.ok("success");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/authcode")
    public ResponseEntity<String> orderAuthCode(@RequestBody AuthCodeRequest authCodeRequest) {
        try{
            if (mailUtil.matchAuthCode(authCodeRequest)) {
                return ResponseEntity.ok("correct");
            } else {
                return ResponseEntity.ok("incorrect");
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}