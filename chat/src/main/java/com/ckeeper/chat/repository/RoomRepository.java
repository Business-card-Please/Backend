package com.ckeeper.chat.repository;

import com.ckeeper.chat.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room,Long> {
}
