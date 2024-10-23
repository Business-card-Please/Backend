package com.ckeeper.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExitRoomRequest {
    String roomId;
    String exiter;
}
