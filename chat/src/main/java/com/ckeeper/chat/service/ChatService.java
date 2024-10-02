package com.ckeeper.chat.service;

import com.ckeeper.chat.dto.CreateRoomRequest;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.repository.ChatRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
   private final ChatRepository chatRepository;

   ChatService(ChatRepository chatRepository) {
       this.chatRepository = chatRepository;
   }

   public Room createChatRoom(CreateRoomRequest dto) {
       Room room = new Room();
       StringBuilder roomId = new StringBuilder();
       roomId.append(dto.getBoardId()).append(dto.getHost()).append(dto.getGuest());
       System.out.println(roomId);
       room.setRoomId(String.valueOf(roomId));
       room.setHost(dto.getHost());
       room.setGuest(dto.getGuest());
       return chatRepository.save(room);
   }
}
