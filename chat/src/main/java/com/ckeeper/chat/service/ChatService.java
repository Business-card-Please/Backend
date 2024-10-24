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
       String[] token = splitRoomId(dto.getRoomId());
       Optional<Room> room = findRoom(createRoomId(token[0],token[1],token[2]));
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

   public Boolean sendMsg(MessageRequest dto){
       Optional<Room> room = findRoom(dto.getRoomId());
       String speaker = dto.getSpeaker();
       String listner = null;

       if(room.isEmpty()){
           room = Optional.of(createRoom(dto.getRoomId()));
       }

       if(speaker.equals(room.get().getHost())){
           listner = room.get().getGuest();
       }else if(speaker.equals(room.get().getGuest())){
           listner = room.get().getHost();
       }

       ChatHistory newChat = new ChatHistory(speaker, dto.getContent(),System.currentTimeMillis());

       room.get().getHistory().add(newChat);

       Map<String,Object> message = new HashMap<>();
       message.put("roomId", dto.getRoomId());
       message.put("speaker",dto.getSpeaker());
       message.put("content",dto.getContent());
       message.put("type","default");

       if(room.get().getHistory().size() == 1){
           room.get().setGuestStatus(true);
           message.put("type","new-room");
           message.put("contract",null);
           message.put("lastMessage",dto.getContent());
       }

       if(listner.equals(room.get().getHost()) && !room.get().getHostStatus()){
           room.get().incrementUnReadHost();
       }else if(listner.equals(room.get().getGuest()) && !room.get().getGuestStatus()){
           room.get().incrementUnReadGuest();
       }

       roomRepository.save(room.get());

       WebSocketSession receiverSession = chatHandler.getSession(listner);

       if (receiverSession != null && receiverSession.isOpen()) {
           chatHandler.handleSendMessage(receiverSession, message);
       }
       return true;
   }

   public List<Map<String, Object>> getRoomList(String nickname){
       List<Room> lst = roomRepository.findByHostOrGuest(nickname);
       List<Map<String, Object>> result = new ArrayList<>();
       for(Room target:lst){
           if(!target.getHistory().isEmpty()) {
               Map<String, Object> chatInfo = new HashMap<>();
               chatInfo.put("id", target.getId());
               chatInfo.put("contract", target.getContract());
               chatInfo.put("lastMessage", target.getHistory().getLast().getContent());

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
