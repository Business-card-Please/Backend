package com.ckeeper.chat.service;

import com.ckeeper.chat.config.WebSocketChatHandler;
import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.repository.RoomRepository;
import org.springframework.stereotype.Service;

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

   private Optional<Room> findRoom(EnterRequest dto){
       return roomRepository.findById(createRoomId(dto));
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
       return findRoom(dto)
               .map(room -> enterRoom(dto, room))
               .orElseGet(() -> createRoom(dto));
   }

//   public Room createOrGetRoom(RoomRequest dto) {
//       String roomId = dto.getBoardId() + dto.getHost() + dto.getGuest();
//       Optional<Room> existingRoom = roomRepository.findByRoomId(roomId);
//
//       if (existingRoom.isPresent()) {
//           return existingRoom.get();
//       } else {
//           Room newRoom = new Room();
//           newRoom.setRoomId(roomId);
//           newRoom.setBoardId(dto.getBoardId());
//           newRoom.setHost(dto.getHost());
//           newRoom.setGuest(dto.getGuest());
//           roomRepository.save(newRoom);
//           return newRoom;
//       }
//   }

//   public void enterRoom(EnterRequest dto){
//       String roomId = dto.getBoardId() + dto.getHost() + dto.getGuest();
//       Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
//       if(dto.getUser().equals(room.getHost())){
//           room.setUnReadHost(0);
//       }else if (dto.getUser().equals(room.getGuest())) {
//           room.setUnReadGuest(0);
//       }
//       roomRepository.save(room);
//   }

    // 메시지 전송 메서드 구현
//    public void sendMsg(MessageRequest dto) {
//        // roomId로 Room 조회
//        Room room = roomRepository.findById(dto.getRoomId())
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        // 새로운 채팅 기록 생성
//        ChatHistory newChat = new ChatHistory(dto.getSpeaker(), dto.getContent());
//
//        // 채팅 기록을 Room의 history에 저장
//        room.getHistory().put(String.valueOf(System.currentTimeMillis()), newChat);
//
//        // 수신자(readStatus) 업데이트
//        if (dto.getSpeaker().equals(room.getHost())) {
//            newChat.getReadStatus().put("host", true);
//            room.incrementUnReadGuest();  // 수신자(guest)의 읽지 않은 메시지 증가
//        } else {
//            newChat.getReadStatus().put("guest", true);
//            room.incrementUnReadHost();  // 수신자(host)의 읽지 않은 메시지 증가
//        }
//
//        // 채팅 기록을 데이터베이스에 저장
//        roomRepository.save(room);
//
//        // 수신자가 현재 WebSocket에 연결되어 있는지 확인
//        WebSocketSession receiverSession = chatHandler.getSession(dto.getListner());
//        if (receiverSession != null && receiverSession.isOpen()) {
//            // 수신자가 연결되어 있다면 실시간으로 메시지 전송
//            chatHandler.handleSendMessage(receiverSession, dto);
//        }
//    }
}
