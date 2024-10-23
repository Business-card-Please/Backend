package com.ckeeper.chat.controller;

import com.ckeeper.chat.dto.ApiResponse;
import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.dto.ExitRoomRequest;
import com.ckeeper.chat.dto.MessageRequest;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.service.ChatService;
import com.ckeeper.chat.util.S2S;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final S2S s2S;

    ChatController(ChatService chatService,S2S s2S) {
        this.chatService = chatService;
        this.s2S = s2S;
    }

    @PostMapping("/room/enter")
    public ResponseEntity<ApiResponse> orderEnterRoom(@RequestBody EnterRequest bodyReq,HttpServletRequest httpReq){
        try{
            this.s2S.sendToAuthServer(httpReq);
            Room result = chatService.createOrEnterRoom(bodyReq);
            return ResponseEntity.ok(new ApiResponse(true,result));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/room/exit")
    public ResponseEntity<ApiResponse> orderExitRoom(@RequestBody ExitRoomRequest bodyReq,HttpServletRequest httpReq){
        try{
            this.s2S.sendToAuthServer(httpReq);
            chatService.exitRoom(bodyReq);
            return ResponseEntity.ok(new ApiResponse(true,""));
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> orderSendMessage(@RequestBody MessageRequest bodyReq,HttpServletRequest httpReq){
        try{
            this.s2S.sendToAuthServer(httpReq);
            chatService.sendOrSaveMsg(bodyReq);
            return ResponseEntity.ok(new ApiResponse(true,""));
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @GetMapping("/room/list")
    public ResponseEntity<ApiResponse> orderSelectList(@RequestParam("nickname") String nickname,HttpServletRequest httpReq){
        try{
            this.s2S.sendToAuthServer(httpReq);
            List<Map<String, Object>> result = chatService.getRoomList(nickname);
            return ResponseEntity.ok(new ApiResponse(true,result));
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }
}
