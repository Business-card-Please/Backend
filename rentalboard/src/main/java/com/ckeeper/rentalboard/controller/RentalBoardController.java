package com.ckeeper.rentalboard.controller;

import com.ckeeper.rentalboard.dto.RentalBoardRequest;
import com.ckeeper.rentalboard.service.RentalBoardService;
import com.ckeeper.rentalboard.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/rentalboard")
public class RentalBoardController {

    private final RentalBoardService rentalBoardService;

    RentalBoardController(RentalBoardService rentalBoardService) {
        this.rentalBoardService = rentalBoardService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> orderBoardCreate(@RequestBody RentalBoardRequest rentalBoardRequest, HttpServletRequest request){
        try{
            rentalBoardService.createBoard(request,rentalBoardRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> orderBoardUpdate(@RequestBody RentalBoardRequest rentalBoardRequest, HttpServletRequest request){
        try{
            rentalBoardService.updateBoard(request,rentalBoardRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

}
