package com.ckeeper.chat.repository;

import com.ckeeper.chat.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRepository extends MongoRepository<Room,String> {
    Optional<Room> findByRoomId(String roomId);
}
