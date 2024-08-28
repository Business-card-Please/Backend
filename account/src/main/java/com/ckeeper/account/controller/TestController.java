package com.ckeeper.account.controller;

import com.ckeeper.account.dto.GenerateAuthCodeRequest;
import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.dto.NicknameRequest;
import com.ckeeper.account.entity.AccountEntity;
import com.ckeeper.account.service.TestService;
import com.ckeeper.account.utils.ApiResponse;
import com.sun.jna.platform.win32.AccCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/dev")
public class TestController {
    @Autowired
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/select-accounts")
    public ResponseEntity<ApiResponse> orderSelectAccounts() {
        try{
            List<AccountEntity> lists = testService.selectCurrentAccountInfo();
            List<String> nicknames = lists.stream().map(AccountEntity::getNickname).collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse(true,nicknames));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/delete-account")
    public ResponseEntity<ApiResponse> orderDeleteAccount(@RequestBody NicknameRequest nicknameRequest) {
        try{
            testService.deleteAccountInfo(nicknameRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }
}
