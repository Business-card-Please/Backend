package com.ckeeper.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private String roomId;
    private String speaker;
    private String listner;
    private String content;
}