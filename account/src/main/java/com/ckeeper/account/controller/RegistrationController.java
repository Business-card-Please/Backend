package com.ckeeper.account.controller;

import com.ckeeper.account.dto.*;
import com.ckeeper.account.exception.InternalServerException;
import com.ckeeper.account.exception.InvalidAuthCodeException;
import com.ckeeper.account.exception.InvalidRequestException;
import com.ckeeper.account.exception.MailSendException;
import com.ckeeper.account.service.RegistrationService;
import com.ckeeper.account.utils.ApiResponse;
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

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse> orderRegistration(@RequestBody RegistrationRequest registrationRequest){
        try{
            registrationService.register(registrationRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(InvalidRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,e.getMessage()));
        } catch(InternalServerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/generate-authcode")
    public ResponseEntity<ApiResponse> orderGenerateAuthCode(@RequestBody GenerateAuthCodeRequest generateAuthCodeRequest) {
        try{
            mailUtil.sendMail(generateAuthCodeRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(MailSendException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,e.getMessage()));
        }catch(InternalServerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }
    @PostMapping("/authcode")
    public ResponseEntity<ApiResponse> orderAuthCode(@RequestBody AuthCodeRequest authCodeRequest) {
        try{
            if (mailUtil.matchAuthCode(authCodeRequest)) {
                return ResponseEntity.ok(new ApiResponse(true,"correct"));
            } else {
                return ResponseEntity.ok(new ApiResponse(true,"incorrect"));
            }
        }catch(InvalidAuthCodeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,e.getMessage()));
        }catch(InternalServerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }
}