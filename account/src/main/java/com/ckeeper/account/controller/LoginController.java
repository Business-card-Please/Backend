package com.ckeeper.account.controller;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.entity.DetailEntity;
import com.ckeeper.account.service.EtcService;
import com.ckeeper.account.service.LoginService;
import com.ckeeper.account.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class LoginController {
    private final LoginService loginService;

    private final EtcService etcService;

    @Value("${redirect.url1}")
    private String url1;

    @Value("${redirect.url2}")
    private String url2;

    public LoginController(LoginService loginService, EtcService etcService) {
        this.loginService = loginService;
        this.etcService = etcService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> orderLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try{
            Boolean result = loginService.checkAccount(loginRequest,response);
            if(result){
                Optional<DetailEntity> info = etcService.getInfo(loginRequest);
                Map<String,String> detail = new HashMap<>();
                detail.put("department1",info.get().getDepartment1());
                detail.put("department2",info.get().getDepartment2());
                return ResponseEntity.ok(new ApiResponse(true,detail));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false,"Invalid nickname or password"));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/check-login")
    public ResponseEntity<ApiResponse> orderCheckLogin(HttpServletRequest request, HttpServletResponse response) {
        try{
            Boolean result = loginService.checkLogin(request,response);
            if(result){
                return ResponseEntity.ok(new ApiResponse(true,"-"));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false,"Expired Token"));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }
}
