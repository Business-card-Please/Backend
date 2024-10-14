package com.ckeeper.chat.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Document(collection = "Room")
public class Room {
    @Id
    private String id;
    private Long boardId;
    private String host;
    private String guest;
    private Map<String,ChatHistory> history = new HashMap<>();
    private Contract contract;
    private int unReadHost;
    private int unReadGuest;

    public void incrementUnReadHost() {
        unReadHost++;
    }

    public void incrementUnReadGuest() {
        unReadGuest++;
    }

    public void setUnReadHost(int unreadForHost) {
        this.unReadHost = unreadForHost;
    }

    public void setUnReadGuest(int unreadForGuest) {
        this.unReadGuest = unreadForGuest;
    }
}
