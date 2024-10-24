package com.ckeeper.chat.model;

import java.util.ArrayList;
import java.util.List;
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
    private List<ChatHistory> history = new ArrayList<>();
    private Contract contract;
    private int unReadHost;
    private int unReadGuest;
    private Boolean hostStatus;
    private Boolean guestStatus;

    public void incrementUnReadHost() {
        unReadHost++;
    }

    public void incrementUnReadGuest() {
        unReadGuest++;
    }
}
