package com.ckeeper.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterRequest {
    Long boardId;
    String host;
    String guest;
    String enterer;
}