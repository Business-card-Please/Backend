package com.ckeeper.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequest {
    Long boardId;
    String host;
    String guest;
}
