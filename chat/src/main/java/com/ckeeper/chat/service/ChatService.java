package com.ckeeper.chat.service;

import com.ckeeper.chat.config.WebSocketChatHandler;
import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.dto.MessageRequest;
import com.ckeeper.chat.model.ChatHistory;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Service
public class ChatService {
   private final RoomRepository roomRepository;
   private final WebSocketChatHandler chatHandler;

   ChatService(RoomRepository roomRepository,WebSocketChatHandler chatHandler) {
       this.roomRepository = roomRepository;
       this.chatHandler = chatHandler;
   }

   private String createRoomId(EnterRequest dto){
       return dto.getBoardId()+"_"+dto.getHost()+"_"+dto.getGuest();
   }

   private Optional<Room> findRoom(String roomId){
       return roomRepository.findById(roomId);
   }

   private Room createRoom(EnterRequest dto){
       Room newRoom = new Room();
       newRoom.setId(createRoomId(dto));
       newRoom.setBoardId(dto.getBoardId());
       newRoom.setHost(dto.getHost());
       newRoom.setGuest(dto.getGuest());
       roomRepository.save(newRoom);
       return newRoom;
   }

   private Room enterRoom(EnterRequest dto, Room room) {
       if (dto.getGuest().equals(room.getHost())) {
           room.setUnReadHost(0);
       } else if (dto.getGuest().equals(room.getGuest())) {
           room.setUnReadGuest(0);
       }
       roomRepository.save(room);
       return room;
   }

   public Room createOrEnterRoom(EnterRequest dto) {
       return findRoom(createRoomId(dto))
               .map(room -> enterRoom(dto, room))
               .orElseGet(() -> createRoom(dto));
   }

   public Boolean sendOrSaveMsg(MessageRequest dto){
       return findRoom(dto.getRoomId())
               .map(room-> sendMsg(dto,room))
               .orElseThrow(() -> new RuntimeException("Room not found"));
   }

   private Boolean sendMsg(MessageRequest dto,Room room){
       String speaker = dto.getSpeaker();
       String listner = null;
       if(speaker.equals(room.getHost())){
           listner = room.getGuest();
       }else if(speaker.equals(room.getGuest())){
           listner = room.getHost();
       }else{
           return false;
       }

       ChatHistory newChat = new ChatHistory(speaker, dto.getContent());
       room.getHistory().put(String.valueOf(System.currentTimeMillis()), newChat);

        if (dto.getSpeaker().equals(room.getHost())) {
            newChat.getReadStatus().put("host", true);
            room.incrementUnReadGuest();
        } else {
            newChat.getReadStatus().put("guest", true);
            room.incrementUnReadHost();
        }

        roomRepository.save(room);

       WebSocketSession receiverSession = chatHandler.getSession(listner);

       if (receiverSession != null && receiverSession.isOpen()) {
           chatHandler.handleSendMessage(receiverSession, dto);
       }
       return true;
   }
}
