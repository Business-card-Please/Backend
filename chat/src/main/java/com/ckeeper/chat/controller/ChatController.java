package com.ckeeper.chat.controller;

import com.ckeeper.chat.dto.ApiResponse;
import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.dto.MessageRequest;
import com.ckeeper.chat.dto.RoomRequest;
import com.ckeeper.chat.repository.RoomRepository;
import com.ckeeper.chat.service.ChatService;
import com.ckeeper.chat.util.S2S;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final RoomRepository roomRepository;
    private final S2S s2S;

    ChatController(ChatService chatService, RoomRepository roomRepository,S2S s2S) {
        this.chatService = chatService;
        this.roomRepository = roomRepository;
        this.s2S = s2S;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> orderCreateRoom(@RequestBody RoomRequest req, HttpServletRequest request, HttpServletResponse response) {
        try{
            this.s2S.sendToAuthServer(request);
            chatService.createOrGetRoom(req);

            return ResponseEntity.ok(new ApiResponse(true,""));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/enter")
    public ResponseEntity<ApiResponse> orderEnterRoom(@RequestBody EnterRequest req,HttpServletRequest request) {
        try{
            this.s2S.sendToAuthServer(request);
            chatService.enterRoom(req);
            return ResponseEntity.ok(new ApiResponse(true,""));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> orderSendMsg(@RequestBody MessageRequest req,HttpServletRequest request){
        try{
            this.s2S.sendToAuthServer(request);
            chatService.sendMsg(req);
            return ResponseEntity.ok(new ApiResponse(true,""));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }
}
