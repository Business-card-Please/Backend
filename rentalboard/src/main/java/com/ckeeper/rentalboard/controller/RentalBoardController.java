package com.ckeeper.rentalboard.controller;

import com.ckeeper.rentalboard.dto.RentalBoardRequest;
import com.ckeeper.rentalboard.dto.RentalBoardSelectRequest;
import com.ckeeper.rentalboard.service.RentalBoardService;
import com.ckeeper.rentalboard.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rentalboard")
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

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> orderBoardDelete(@RequestBody RentalBoardRequest rentalBoardRequest, HttpServletRequest request){
        try{
            rentalBoardService.deleteBoard(request,rentalBoardRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/select")
    public ResponseEntity<ApiResponse> orderBoardSelect(@RequestBody RentalBoardSelectRequest rentalBoardSelectRequest, @RequestParam("data1") String department1,@RequestParam("data2") String department2, HttpServletRequest request){
        try{
            System.out.println(department1+" "+department2);
//            rentalBoardService.selectBoard(request,rentalBoardSelectRequest);
            return ResponseEntity.ok(new ApiResponse(true,"-"));
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
