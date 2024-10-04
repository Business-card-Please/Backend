package com.ckeeper.chat.controller;

import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.dto.MessageRequest;
import com.ckeeper.chat.dto.RoomRequest;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.repository.RoomRepository;
import com.ckeeper.chat.service.ChatService;
import org.springframework.stereotype.Controller;
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
    public Room orderCreateRoom(@RequestBody RoomRequest req) {
        return chatService.createOrGetRoom(req);
    }

    @PostMapping("/enter")
    public void orderEnterRoom(@RequestBody EnterRequest req) {
        chatService.enterRoom(req);
    }

    @PostMapping("/send")
    public void orderSendMsg(@RequestBody MessageRequest req){
        chatService.sendMsg(req);
    }
}
