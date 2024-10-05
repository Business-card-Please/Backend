package com.ckeeper.chat.controller;

import com.ckeeper.chat.dto.ApiResponse;
import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.dto.MessageRequest;
import com.ckeeper.chat.dto.RoomRequest;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.repository.RoomRepository;
import com.ckeeper.chat.service.ChatService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final RoomRepository roomRepository;

    ChatController(ChatService chatService, RoomRepository roomRepository) {
        this.chatService = chatService;
        this.roomRepository = roomRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> orderCreateRoom(@RequestBody RoomRequest req, HttpServletResponse response) {
        try{
            chatService.createOrGetRoom(req);
            // CORS 헤더 추가 확인
//            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5500");
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//
//            // 헤더 디버깅 로그 출력
//            System.out.println("Access-Control-Allow-Origin: " + response.getHeader("Access-Control-Allow-Origin"));

            return ResponseEntity.ok(new ApiResponse(true,""));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/enter")
    public void orderEnterRoom(@RequestBody EnterRequest req) {
        System.out.println("어디까지 왔나?222_enter");
        chatService.enterRoom(req);
    }

    @PostMapping("/send")
    public void orderSendMsg(@RequestBody MessageRequest req){
        System.out.println("어디까지 왔나?222_send");
        chatService.sendMsg(req);
    }
}
