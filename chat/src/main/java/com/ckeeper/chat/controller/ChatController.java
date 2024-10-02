package com.ckeeper.chat.controller;

import com.ckeeper.chat.dto.CreateRoomRequest;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public Room orderCreateRoom(@RequestBody CreateRoomRequest req) {
        return chatService.createChatRoom(req);
    }
}
