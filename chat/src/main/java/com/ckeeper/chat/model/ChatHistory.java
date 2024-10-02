package com.ckeeper.chat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatHistory {
    private String speaker;
    private String content;
    private int status;
}
