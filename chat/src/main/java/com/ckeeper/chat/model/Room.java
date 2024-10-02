package com.ckeeper.chat.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Document(collection = "Room")
public class Room {
    @Id
    private String roomId;
    private String host;
    private String guest;
    private Map<String,ChatHistory> history;
}
