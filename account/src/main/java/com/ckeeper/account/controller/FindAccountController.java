package com.ckeeper.account.controller;

import com.ckeeper.account.dto.GenerateAuthCodeRequest;
import com.ckeeper.account.dto.ResetPasswordRequest;
import com.ckeeper.account.exception.InternalServerException;
import com.ckeeper.account.exception.MailSendException;
import com.ckeeper.account.service.FindAccountService;
import com.ckeeper.account.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/account")
public class FindAccountController {
    @Autowired
    private final FindAccountService findAccountService;

    @Autowired
    public FindAccountController(FindAccountService findAccountService) {
        this.findAccountService = findAccountService;
    }

    @PostMapping("/find/nickname")
    public ResponseEntity<ApiResponse> orderFindNickname(@RequestBody GenerateAuthCodeRequest generateAuthCodeRequest){
        try{
            findAccountService.findNickname(generateAuthCodeRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(MailSendException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,e.getMessage()));
        }catch(InternalServerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/reset/pw")
    public ResponseEntity<ApiResponse> orderResetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        try{
            Boolean result = findAccountService.resetPassword(resetPasswordRequest);
            if(result){
                return ResponseEntity.ok(new ApiResponse(true,"-"));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false,"Equal password"));
            }

        }catch(InternalServerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

}
