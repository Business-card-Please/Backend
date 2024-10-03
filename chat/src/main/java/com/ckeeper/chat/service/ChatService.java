package com.ckeeper.chat.service;

import com.ckeeper.chat.dto.EnterRequest;
import com.ckeeper.chat.dto.RoomRequest;
import com.ckeeper.chat.model.Room;
import com.ckeeper.chat.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
   private final RoomRepository roomRepository;

   ChatService(RoomRepository roomRepository) {
       this.roomRepository = roomRepository;
   }

   public Room createOrGetRoom(RoomRequest dto) {
       String roomId = dto.getBoardId() + dto.getHost() + dto.getGuest();
       Optional<Room> existingRoom = roomRepository.findByRoomId(roomId);

       if (existingRoom.isPresent()) {
           return existingRoom.get();
       } else {
           Room newRoom = new Room();
           newRoom.setRoomId(roomId);
           newRoom.setBoardId(dto.getBoardId());
           newRoom.setHost(dto.getHost());
           newRoom.setGuest(dto.getGuest());
           roomRepository.save(newRoom);
           return newRoom;
       }
   }

   public void enterRoom(EnterRequest dto){
       String roomId = dto.getBoardId() + dto.getHost() + dto.getGuest();
       Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
       if(dto.getUser().equals(room.getHost())){
           room.setUnReadHost(0);
       }else if (dto.getUser().equals(room.getGuest())) {
           room.setUnReadGuest(0);
       }
       roomRepository.save(room);
   }
}
