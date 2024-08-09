package com.ckeeper.account.controller;

import com.ckeeper.account.dto.AuthCodeRequest;
import com.ckeeper.account.dto.RegistrationRequest1;
import com.ckeeper.account.service.RegistrationService;
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
    private final MailUtil mailUtil;

    @Autowired
    public RegistrationController(RegistrationService registrationService,MailUtil mailUtil) {
        this.registrationService = registrationService;
        this.mailUtil = mailUtil;
    }
    @PostMapping("/register1")
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
    @PostMapping("/authcode")
    public ResponseEntity<String> orderAuthCode(@RequestBody AuthCodeRequest authCodeRequest) {
        try{
            this.mailUtil.sendMail(authCodeRequest);
            return ResponseEntity.ok("success");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}