package com.ckeeper.account.controller;

import com.ckeeper.account.dto.LoginRequest;
import com.ckeeper.account.entity.DetailEntity;
import com.ckeeper.account.service.EtcService;
import com.ckeeper.account.service.LoginService;
import com.ckeeper.account.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class LoginController {
    private final LoginService loginService;

    private final EtcService etcService;

    @Value("${redirect.url}")
    private String url;

    public LoginController(LoginService loginService, EtcService etcService) {
        this.loginService = loginService;
        this.etcService = etcService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> orderLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try{
            Boolean result = loginService.checkAccount(loginRequest,response);
            if(result){
                return ResponseEntity.ok(new ApiResponse(true,"-"));
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
                String originUrl = request.getHeader("X-Original-URI");
                if(originUrl.contains("/rentalboard/select")){
                    Optional<DetailEntity> test = etcService.getAccountInfo(request);
                    String data1 = URLEncoder.encode(test.get().getDepartment1(), StandardCharsets.UTF_8);
                    String data2 = URLEncoder.encode(test.get().getDepartment2(), StandardCharsets.UTF_8);
                    String redirectUrl = String.format("%s/rentalboard/select?data1=%s&data2=%s",url,data1,data2);
                    return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, redirectUrl)
                            .build();
                }else if(originUrl.contains("/rentalboard/create")) {
                    return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, url+"/rentalboard/create")
                            .build();
                }else if(originUrl.contains("/rentalboard/delete")){
                    return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, url+"/rentalboard/delete")
                            .build();
                }else if(originUrl.contains("/rentalboard/update")){
                    return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                            .header(HttpHeaders.LOCATION, url+"/rentalboard/update")
                            .build();
                }
                return ResponseEntity.ok(new ApiResponse(true,"-"));
            }else{
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false,"Expired Token"));
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }
}
