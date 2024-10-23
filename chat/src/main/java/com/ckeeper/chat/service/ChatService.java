package com.ckeeper.chat.service;

import com.ckeeper.chat.config.WebSocketChatHandler;
import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.dto.ExitRoomRequest;
import com.ckeeper.chat.dto.MessageRequest;
import com.ckeeper.chat.model.ChatHistory;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.repository.RoomRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
       newRoom.setHostStatus(false);
       newRoom.setGuestStatus(false);
       roomRepository.save(newRoom);
       return newRoom;
   }

   private Room enterRoom(EnterRequest dto, Room room) {
       String enterer = dto.getEnterer();
       if (enterer.equals(room.getHost())) {
           room.setUnReadHost(0);
           room.setHostStatus(true);
       } else if (enterer.equals(room.getGuest())) {
           room.setUnReadGuest(0);
           room.setGuestStatus(true);
       }
       roomRepository.save(room);
       return room;
   }

    public void exitRoom(ExitRoomRequest dto) {
        findRoom(dto.getRoomId())
                .ifPresentOrElse(room -> {
                    if (dto.getExiter().equals(room.getHost())) {
                        room.setHostStatus(false);
                    } else if (dto.getExiter().equals(room.getGuest())) {
                        room.setGuestStatus(false);
                    }
                    roomRepository.save(room);
                }, () -> {
                    throw new RuntimeException("Room not found");
                });
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

       ChatHistory newChat = new ChatHistory(speaker, dto.getContent(),System.currentTimeMillis());
       room.getHistory().put(room.getHistory().size()+1, newChat);

        if (dto.getSpeaker().equals(room.getHost())) {
            room.incrementUnReadGuest();
        } else {
            room.incrementUnReadHost();
        }

        roomRepository.save(room);

       WebSocketSession receiverSession = chatHandler.getSession(listner);

       if (receiverSession != null && receiverSession.isOpen()) {
           chatHandler.handleSendMessage(receiverSession, dto);
       }
       return true;
   }

   public List<Map<String, Object>> getRoomList(String nickname){
       List<Room> lst = roomRepository.findByHostOrGuest(nickname);
       List<Map<String, Object>> result = new ArrayList<>();
       for(Room target:lst){
           if(target.getHistory().size() > 0) {
               Map<String, Object> chatInfo = new HashMap<>();
               chatInfo.put("id", target.getId());
               chatInfo.put("boardId", target.getBoardId());
               chatInfo.put("host", target.getHost());
               chatInfo.put("guest", target.getGuest());
               chatInfo.put("contract", target.getContract());
               chatInfo.put("lastMessage", target.getHistory().get(target.getHistory().size()).getContent());

               int unRead = target.getUnReadHost();
               if (nickname.equals(target.getGuest())) {
                   unRead = target.getUnReadGuest();
               }

               chatInfo.put("unRead", unRead);
               result.add(chatInfo);
           }

       }
       return result;
   }
}
