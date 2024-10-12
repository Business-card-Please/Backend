package com.ckeeper.rentalboard.controller;

import com.ckeeper.rentalboard.dto.RentalBoardRequest;
import com.ckeeper.rentalboard.dto.RentalBoardSelectRequest;
import com.ckeeper.rentalboard.entity.RentalBoardEntity;
import com.ckeeper.rentalboard.service.RentalBoardService;
import com.ckeeper.rentalboard.utils.ApiResponse;
import com.ckeeper.rentalboard.utils.S2S;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rentalboard")
public class RentalBoardController {
    private final RentalBoardService rentalBoardService;
    private final S2S s2S;

    RentalBoardController(RentalBoardService rentalBoardService,S2S s2S) {
        this.rentalBoardService = rentalBoardService;
        this.s2S = s2S;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> orderBoardCreate(@RequestBody RentalBoardRequest rentalBoardRequest, HttpServletRequest request){
        try{
            s2S.sendToAuthServer(request);
            rentalBoardService.createBoard(request,rentalBoardRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> orderBoardUpdate(@RequestBody RentalBoardRequest rentalBoardRequest, HttpServletRequest request){
        try{
            s2S.sendToAuthServer(request);
            rentalBoardService.updateBoard(request,rentalBoardRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> orderBoardDelete(@RequestBody RentalBoardRequest rentalBoardRequest, HttpServletRequest request){
        try{
            s2S.sendToAuthServer(request);
            rentalBoardService.deleteBoard(request,rentalBoardRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/select")
    public ResponseEntity<ApiResponse> orderBoardSelect(@RequestBody RentalBoardSelectRequest rentalBoardSelectRequest,HttpServletRequest request){
        try{
            s2S.sendToAuthServer(request);
            Map<String,Object> result = rentalBoardService.selectBoard(rentalBoardSelectRequest);
            return ResponseEntity.ok(new ApiResponse(true,result));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
