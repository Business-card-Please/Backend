package com.ckeeper.account.controller;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.service.LoginService;
import com.ckeeper.account.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/account")
public class LoginController {
    @Autowired
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> orderLogin(@RequestBody LoginRequest loginRequest) {
        try{
            Boolean result = loginService.checkLogin(loginRequest);
            if(result){
                return ResponseEntity.ok(new ApiResponse(true,"-"));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false,"Invalid email or password"));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }
}
