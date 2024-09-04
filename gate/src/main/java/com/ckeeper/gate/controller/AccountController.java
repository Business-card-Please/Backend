package com.ckeeper.gate.controller;

import com.ckeeper.gate.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/")
public class AccountController {
    @PostMapping("/auth-check")
    ResponseEntity<ApiResponse> orderCheckJwt(HttpServletRequest request){

    }
}
