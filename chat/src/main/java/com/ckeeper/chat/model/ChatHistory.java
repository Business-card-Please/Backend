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
    private Long datetime;

    public ChatHistory(String speaker, String content, Long datetime) {
        this.speaker = speaker;
        this.content = content;
        this.datetime = datetime;
    }
}
