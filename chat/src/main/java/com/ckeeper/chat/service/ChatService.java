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
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.security.core.parameters.P;
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

   private String createRoomId(String boardId, String host, String guest){
       return boardId+"_"+host+"_"+guest;
   }

   private String[] splitRoomId(String roomId){
       return roomId.split("_");
   }

   private Optional<Room> findRoom(String roomId){
       return roomRepository.findById(roomId);
   }

   private Room createRoom(String roomId){
       Room newRoom = new Room();
       String[] token = splitRoomId(roomId);

       newRoom.setId(createRoomId(token[0],token[1],token[2]));
       newRoom.setBoardId(Long.valueOf(token[0]));
       newRoom.setHost(token[1]);
       newRoom.setGuest(token[2]);
       newRoom.setHostStatus(false);
       newRoom.setGuestStatus(false);

       return newRoom;
   }

   public Map<String,Object> enterRoom(EnterRequest dto) {
       Optional<Room> room = findRoom(createRoomId(String.valueOf(dto.getBoardId()),dto.getHost(),dto.getGuest()));
       if(room.isPresent()){
           String enterer = dto.getEnterer();
           if (enterer.equals(room.get().getHost())) {
               room.get().setUnReadHost(0);
               room.get().setHostStatus(true);
           } else if (enterer.equals(room.get().getGuest())) {
               room.get().setUnReadGuest(0);
               room.get().setGuestStatus(true);
           }
           roomRepository.save(room.get());
           Map<String,Object> ret = new HashMap<>();
           ret.put("roomId",room.get().getId());
           ret.put("contract",room.get().getContract());
           ret.put("history",room.get().getHistory());
           return ret;
       }
       return null;
   }

   public void exitRoom(ExitRoomRequest dto) {
       Optional<Room> room = findRoom(dto.getRoomId());
       if(room.isPresent()){
           if (dto.getExiter().equals(room.get().getHost())) {
               room.get().setHostStatus(false);
           } else if (dto.getExiter().equals(room.get().getGuest())) {
               room.get().setGuestStatus(false);
           }
           roomRepository.save(room.get());
       } else {
           throw new RuntimeException("Room not found");
       }
   }

   private Boolean sendMsg(MessageRequest dto){
       Optional<Room> room = findRoom(dto.getRoomId());
       if(room.isPresent()){

       }else{
            room = Optional.of(createRoom(dto.getRoomId()));
       }
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
       room.getHistory().add(newChat);

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
               chatInfo.put("lastMessage", target.getHistory().getLast());

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
