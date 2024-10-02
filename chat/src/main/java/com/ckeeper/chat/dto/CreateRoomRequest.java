package com.ckeeper.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequest {
    String boardId;
    String host;
    String guest;
}
