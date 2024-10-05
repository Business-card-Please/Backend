package com.ckeeper.chat.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatHistory {
    private String speaker;
    private String content;
    private Map<String, Boolean> readStatus = new HashMap<>();

    public ChatHistory(String speaker, String content) {
        this.speaker = speaker;
        this.content = content;
        this.readStatus.put("host", false);
        this.readStatus.put("guest", false);
    }
}
