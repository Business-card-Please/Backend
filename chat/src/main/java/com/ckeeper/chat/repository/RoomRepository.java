package com.ckeeper.chat.repository;

import com.ckeeper.chat.model.Room;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RoomRepository extends MongoRepository<Room,String> {
    @Query("{ '$or': [ {'host': ?0}, {'guest': ?0} ] }")
    List<Room> findByHostOrGuest(String nickname);
}
